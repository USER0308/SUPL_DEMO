'use strict';
var log4js = require('log4js');
var logger = log4js.getLogger('SampleWebApp');
var express = require('express');
var http = require('http');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var expressJWT = require('express-jwt');
var jwt = require('jsonwebtoken');
var helper = require('./common/helper.js');
var db = require("./common/db.js");
var config = require('./config/config.json');
var bearerToken = require('express-bearer-token');
var cors = require('cors');
var util = require('util');
var csn = require('./common/changeState.js');

var binq = require("./bank/bank-inward-query.js")
var bnoq = require("./bank/bank-nostro-query.js")
var bouq = require("./bank/bank-outward-query.js")
var btxi = require("./bank/bank-tx-input.js")
var bupc = require("./bank/update-compst.js")

var uboq = require("./user/user-bal-query.js")
var uinq = require("./user/user-inward-query.js")
var uouq = require("./user/user-outward-query.js")
var utxi = require("./user/user-tx-input.js")
var app = express();
var path = require("path");
var query = require('./common/query.js');

var host = 'localhost';
var port = 80;
csn.changeComNos();
///////////////////////////////////////////////////////////////////////////////
//////////////////////////////// SET CONFIGURATONS ////////////////////////////
///////////////////////////////////////////////////////////////////////////////
app.use(express.static('./react-web/dist'));

app.options('*', cors());
app.use(cors());
//support parsing of application/json type post data
app.use(bodyParser.json());
//support parsing of application/x-www-form-urlencoded post data
app.use(bodyParser.urlencoded({
	extended: true
}));
// set secret variable
app.set('secret', 'thisismysecret');
app.use(expressJWT({
	secret: 'thisismysecret'
}).unless({
	path: ['/users', '/login','/favicon.ico','/bank','/bank/*','/user','/user/*']
}));
app.use(bearerToken());
app.use(function (req, res, next) {

	if (req.originalUrl.indexOf('/users') >= 0 || req.originalUrl.indexOf('/login') >= 0 || req.originalUrl.indexOf('/bank') >= 0 || req.originalUrl.indexOf('/user') >= 0) {
		return next();
	}

	var token = req.query.token;
	jwt.verify(token, app.get('secret'), function (err, decoded) {
		if (err) {
			res.send({
				success: false,
				message: 'Failed to authenticate token. Make sure to include the ' +
				'token returned from /users call in the authorization header ' +
				' as a Bearer token'
			});
			return;
		} else {
			// add the decoded user name and org name to the request object
			// for the downstream code to use
			req.username = decoded.username;
			req.orgname = decoded.orgName;
			logger.debug(util.format('Decoded from JWT token: username - %s, orgname - %s', decoded.username, decoded.orgName));
			return next();
		}
	});
});
///////////////////////////////////////////////////////////////////////////////
//////////////////////////////// START SERVER /////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
var server = http.createServer(app).listen(port, function () { });
logger.info('****************** SERVER STARTED ************************');
logger.info('**************  http://' + host + ':' + port +
	'  ******************');
server.timeout = 240000;

function getErrorMessage(field) {
	var response = {
		success: false,
		message: field + ' field is missing or Invalid in the request'
	};
	return response;
}

app.get('*', function (request, response){
  response.sendFile(path.resolve(__dirname, 'react-web/dist', 'index.html'));
});


//用户汇出查询
app.post('/UserOutwardQuery', function (req, res) {
	logger.debug('==================== QUERY BY CHAINCODE ==================');
	uouq.UserOutwardQuery(req, res);
});
app.post('/UserOutQueryCdt', function (req, res) {
	logger.debug('==================== QUERY BY CHAINCODE ==================');
	uouq.UserOutQueryCdt(req, res);
});


//用户交易录入
app.post('/UserTxInput', function (req, res) {
	logger.debug('==================== QUERY BY CHAINCODE ==================');
	utxi.UserTxInput(req, res);
});


//用户余额查询
app.post('/UserBostroQuery', function (req, res) {
	logger.debug('==================== QUERY BY CHAINCODE ==================');
	uboq.UserBalQuery(req, res);
});


//用户汇入查询
app.post('/UserInwardQuery', function (req, res) {
	logger.debug('==================== QUERY BY CHAINCODE ==================');
	uinq.UserInwardQuery(req, res);
});
app.post('/UserInQueryCdt', function (req, res) {
	logger.debug('==================== QUERY BY CHAINCODE ==================');
	uinq.UserInQueryCdt(req, res);
});


//银行汇出查询
app.post('/BankOutwardQuery', function (req, res) {
	logger.debug('==================== QUERY BY CHAINCODE ==================');
	bouq.BankOutwardQuery(req, res);
});
app.post('/BankOutQueryCdt', function (req, res) {
	logger.debug('==================== QUERY BY CHAINCODE ==================');
	bouq.BankOutQueryCdt(req, res);
});


//银行汇入查询
app.post('/BankInwardQuery', function (req, res) {
	logger.debug('==================== QUERY BY CHAINCODE ==================');
	binq.BankInwardQuery(req, res);
});
app.post('/BankInQueryCdt', function (req, res) {
	logger.debug('==================== QUERY BY CHAINCODE ==================');
	binq.BankInQueryCdt(req, res);
});


//更新状态
app.post('/UpdateCOMPST', function (req, res) {
	logger.debug('==================== QUERY BY CHAINCODE ==================');
	bupc.updateCOMPST(req, res);
});


//银行余额查询
app.post('/BankNostroQuery', function (req, res) {
	logger.debug('==================== QUERY BY CHAINCODE ==================');
	bnoq.BankNostroQuery(req, res);
});

app.post('/login', function (req, res) {
	logger.debug('==================== login ==================');
	logger.debug(req.body);
	var username = req.body.username;
	var password = req.body.password;
	var usertype = req.body.usertype;

	var sql = 'SELECT * FROM ?? where user_name=?';
	var param = ['td_user', username];

	logger.debug(req.body.username);
	logger.debug(req.body.password);
	logger.debug(req.body.usertype);
	db.exec(sql, param, function (err, result) {
		if (err) {

			console.log('[SELECT ERROR] - ', err.message);

			res.json({
				success: false,
				bank: null,
				message: null
			})
			return;
		}

		if (!result[0]) {
			res.json({
				success: false,
				bank: null,
				message: null
			})
			return;
		}

		//验证
		if (result[0]['user_password'] != password) {
			logger.error("worng password.")
			res.json({
				success: false,
				bank: null,
				message: null
			})
			return;
		}
		var token = jwt.sign({
			exp: Math.floor(Date.now() / 1000) + parseInt(config.jwt_expiretime),
			username: username,
			orgName: result[0]['belong_org']
		}, app.get('secret'));
		console.log(token);

		if (result[0]['user_type'] == "1") {
			res.json({
				success: true,
				type:"1",
				bank: result[0]['bank'],
				message: token
			});
			return;
		}else{
			res.json({
				success: true,
				type:"2",
				bank: result[0]['bank'],
				message: token
			});
			return;
		}
	});
});

//根据区块编号查询获得区块
app.get('/queryBlockInfoHeight', function (req, res) {
	logger.debug('==================== GET BLOCK BY NUMBER ==================');

	let blockId = req.query.curHeight;
	let peer = "peer1";

	logger.debug('BlockID : ' + blockId);
	logger.debug('Peer : ' + peer);
	if (!blockId) {
		res.json(getErrorMessage('\'blockId\''));
		return;
	}

	query.getBlockByNumber(peer, blockId, req.username, req.orgname)
		.then(function (message) {

			var datas = message.data.data;
			var header = message.header;
			var txData = new Array();
			var create_ts;

			var height = message.header.number.low;
			console.log("`````````````````````````` height: " + height);
			
			if (height > 1) {
				for (var i = 0; i < datas.length; i++) {
					var info = datas[i].payload.header.channel_header;
					var txValue = datas[i].payload.data.actions[0].payload.action.proposal_response_payload.extension.results.ns_rwset[1].rwset.writes[0].value;
					// if (txValue == undefined || !txValue) {
					// 	console.error("can not parse block info.");
					// 	res.end("failed");
					// 	return;
					// }
					var txInfo = JSON.parse(txValue);
					if (txInfo.docType == "nostro") {
						txData.push({
							"docType": "nostro",
							"actid": txInfo.actid, "bkcode": txInfo.bkcode,
							"clrbkcde": txInfo.clrbkcde, "curcde": txInfo.curcde,
							"nostrobal": txInfo.nostrobal
						});
						create_ts = info.timestamp;
					} else {
						txData.push({
							"docType": "txinstr",
							"instrId": txInfo.instrid, "txndat": txInfo.txndat, "bkcode": txInfo.bkcode,
							"actnofrom": txInfo.actnofrom, "clrbkcde": txInfo.clrbkcde, "actnoto": txInfo.actnoto,
							"txamt": txInfo.txamt, "compst": txInfo.compst, "curcde": txInfo.curcde,
							"txntime": txInfo.txntime
						});
						if (i == (datas.length - 1)) {
							create_ts = info.timestamp;
						}
					}

				}
			} else {//height==0
				create_ts = datas[datas.length - 1].payload.header.channel_header.timestamp;
				txData = null;
			}

			var result = {
				 "create_ts": create_ts, "count": datas.length, "txdata": txData,
				"height": height, "cur_hash": header.data_hash, "pre_hash": header.previous_hash
			};
			res.send(result);
		});
});

//Query for Channel Information
app.get('/queryLastBlock', function (req, res) {
	logger.debug('================ GET CHANNEL INFORMATION ======================');
	let peer = "peer1";

	query.getChainInfo(peer, req.username, req.orgname).then(
		function (message) {
			
			var height = message.height.low;
			var coffset = message.currentBlockHash.offset;
			var climit = message.currentBlockHash.limit;
			var poffset = message.previousBlockHash.offset;
			var plimit = message.previousBlockHash.limit;

			var currentBlockHash = Buffer.from(message.currentBlockHash.buffer.slice(coffset, climit)).toString("hex");
			var previousBlockHash = Buffer.from(message.previousBlockHash.buffer.slice(poffset, plimit)).toString("hex");
			logger.debug("height:" + height);
			logger.debug("currentBlockHash:" + currentBlockHash);
			logger.debug("previousBlockHash:" + previousBlockHash);
			var result = { "height": height, "currentBlockHash": currentBlockHash, "previousBlockHash": previousBlockHash };
			res.send(result);
		});

});

app.get('/getBlockByHash',function(req,res){
	logger.debug('================ getBlockByHash ======================');
	var hash=req.query.hash;
	let peer = "peer1";//["localhost:7051"]
	query.getBlockByHash(peer,hash,req.username,req.orgname).then(function(message){

		console.log(".................-.=._.+. "+message);
		
		var result = { "height": message.header.number.low, "currentBlockHash": message.header.data_hash, "previousBlockHash": message.header.previous_hash};

		res.send(result);

	});
});
