import sys
import simplejson as json
import time
from cmislib.model import CmisClient, Repository

if len(sys.argv) != 5:
	print "Syntax: makecontent.py <hostname> <foldername> <numofiterations> <doupdate>"
	print "eg: python makecontent.py localhost site1 100 Y"
	sys.exit()

hostname = sys.argv[1]
rootfoldername = sys.argv[2] 
iterations = int(sys.argv[3])
doupdate = sys.argv[4]

weprep = ""
weprep = weprep + "<style>"
weprep = weprep + "   table, th, td { border: 1px solid black; }"
weprep = weprep + "   .forat { border: 1px solid blue; }"
weprep = weprep + "</style>"
weprep = weprep + "<hr>"
	
body = "This is a project to show system load. Using a share amp to expose create update delete behaviors, using a webscript and paper.js with ajax calls.  This content is created on the fly using the awesome cmislib (thanks Jeff Potts) to build this content"

ftab = "<table width=500 height=30 border=2>"
ftab = ftab + "<tr>"
ftab = ftab + "<td>Alfresco Hackathon</td><td>marsbard and digcat</td><td>DataVisProject</td><td>1</td>"
ftab = ftab + "</tr>"
ftab = ftab + "<tr>"
ftab = ftab + "<td colspan=4>" + body + "</td>"
ftab = ftab + "</tr>"
ftab = ftab + "</table>"
weprep = weprep + ftab

cmurl = hostname + '/alfresco/cmisatom'
client = CmisClient(cmurl,'admin','admin')

sitename =  'alfresco-hackathon'
basepath = '/Sites/' + sitename + '/documentLibrary'
rootofwn = basepath + '/' + rootfoldername
repo = client.defaultRepository

try:
	rootsite = repo.getObjectByPath(rootofwn)
except:	
	rootsite = repo.getObjectByPath(basepath)
	basefolder = rootsite.createFolder(rootfoldername)
	try:
		rootsite = repo.getObjectByPath(rootofwn)
	except:	
		print "Error: Unable to create folder"

rootfolder = repo.getObjectByPath(rootofwn)
props = rootfolder.getProperties()

for k,v in props.items():
  	print '%s:%s' % (k,v)

listofdocs = []

for x in range(0,iterations):
 	docname = "Datavis-" + str(time.clock())
	try:
		newdoc = repo.createDocumentFromString(docname, parentFolder=rootfolder, contentString=weprep, contentType='text/html')
		props = newdoc.getProperties()
		listofdocs.append(docname)
		for k,v in props.items():
			print '%s:%s' % (k,v)
	except:
		print "Document Exists"

if doupdate == "Y":
	for doc in listofdocs:
		name = doc
		querySimpleSelect = "SELECT * FROM cmis:document where cmis:name like '" + name + "'"
		resultSet = repo.query(querySimpleSelect)
		for result in resultSet:
			print result.name 
		
			if (result.name == name):
				props = result.properties	
				for k,v in props.items():
					print '%s:%s' % (k,v)
				newprop = { 'cmis:description': 'some new description'} 
				doc = repo.getObjectByPath(rootofwn + '/' + result.name)	
				doc.updateProperties(newprop)

if doupdate != "Y":
	rootfolder = repo.getObjectByPath(rootofwn)
	rootfolder.deleteTree()
