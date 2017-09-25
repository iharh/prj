default xml namespace = "http://clarabridge.com/fx/config";

var configXml = new XML(input);

var str = ""
for each (var m in configXml..module.(@path == "morph"))
{
    m.@path = "morph5";
}

// output = "abc";
// output = str;
output = configXml.toXMLString();
