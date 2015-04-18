# alfresco-global-hackathon-15-datavis
the idea is to take the behaviours layer and surface some of the underlying
activity in an alfresco instance for visualisation

Initially I am thinking to look at when nodes are created, modified and
destroyed. Additionally to look at the document type and the content model as
examples of drilling down into further metadata about each document.

There could be other activities we want to look at. Discuss. (But I am
specifically thinking of high volume activites that are hard to otherwise
visualise)

We don't want to incur overhead when nobody is listening, so we make clients
subscribe, and only when there is someone listening we enable the backend
monitoring.

Once a listener is there I would like to look at how easy the Tomcat websockets
implementation is to use; for this kind of data we would need to provide
something like that.
