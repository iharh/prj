@echo off
setlocal
:://attributes/attributeValues/attributeValue
:://S:Envelope/S:Body/ns2:processMultiVerbatimDocumentResponse

::set PAT="//*/*/*/return/verbatimSet/verbatim/attributes/attributeValues/attributeName"
set PAT="//*/*/*/return/verbatimSet/verbatim/sentences/sentence/attributes/attributeValues/attributeName"

call xml.bat sel -t -v %PAT% 2.xml
endlocal
