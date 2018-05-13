var express =require("express");
var bodyParser = require('body-parser');
var path  = require('path');
var app = express();

app.use(express.static(__dirname + "/dist"));
app.use(bodyParser.urlencoded({
	extended: true
}));


// BrowserHistory code
app.get('*', function (request, response){
  response.sendFile(path.resolve(__dirname, 'dist', 'index.html'));
});

var server = app.listen(3001,function (req,res) {
	var host = server.address().address;
	var port = server.address().port;
	  console.log('Example app listening at http://%s:%s', host, port);
});

