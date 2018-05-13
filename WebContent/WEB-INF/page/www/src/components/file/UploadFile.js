require('normalize.css/normalize.css');
require('styles/App.css');

import React from 'react';
import Header from '../Header';
import Celer from '../Celer';
import ReactDOM from 'react-dom';

import {browserHistory} from 'react-router';





var token=sessionStorage.getItem("token");
var  sessionId=sessionStorage.getItem("sessionId");
var  uploadUserId=sessionStorage.getItem("userId");

class UploadFile extends React.Component {

  constructor(){
    super();
 
    this.state = {
      departments:{},
       allowRank:1,
       fileId:'',
       description:''
        //rank和department都是select框，得先赋值1，否则不触发onChange方法，导致无值
    }
    //this.handleUpload = this.handleUpload.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.InputChange = this.InputChange.bind(this);
    this.handleChange = this.handleChange.bind(this);
    //每一个使state变动的方法都必须指定this，即加上后续的.bind(this)
  }

componentWillMount() {
  sessionStorage.setItem("pageInt",10);
}


uploadFile2(){


  var uploadForm2 = ReactDOM.findDOMNode(this.refs.uploadForm);
  console.log(this.refs);
  //通过dom树查到upload文件值
  console.log(uploadForm2);
  var formData = new FormData(uploadForm2);
	var selectedDep='';
  console.log(this.state.departments)
  console.log(deptObj);
  // $("input:checkbox:checked[name='departments']").each(
  //   function (){
  //     var checkBoxVal=$(this).val();
  //     console.log(checkBoxVal);
	// 		selectedDep+=checkBoxVal+',';
		
	// 	}
  // );
  var deptObj = this.state.departments;
  for(var key in deptObj){
    console.log("=====key======",key);
    if(deptObj[key]){
      selectedDep+=key+',';
    }
  }
  
  selectedDep = selectedDep.substr(0,selectedDep.length-1);
  console.log("===========",selectedDep);

  //将uploadForm转为formData格式
  formData.append("userId",uploadUserId);
  formData.append("fileId",this.state.fileId);
  formData.append("allowRank",this.state.allowRank);
  formData.append("allowDep",selectedDep);

  formData.append("token",token);
  
  formData.append("sessionId",sessionId);
  formData.append("description",this.state.description);
 
  
 

  window.formPost('uploadFile',formData,function(data){

  console.log(data);
  window.location.href='/file/query';
       //成功后可以弹出返回信息，先用alert，弹出框我没找到合适的轮子wwwwwww
      //  window.location.href='/file/query';
       //跳转，应该写在轮子里的，放外面容易出现情况把alert这步跳过，无法显示后台返回信息
    }); 
  
}

InputChange(e){
  const target = e.target;
  const value =  target.value;
  const checked =  target.checked;
  var obj=this.state.departments;
  Object.defineProperty(obj,value,{value:checked,configurable:true,enumerable:true});
}
handleInputChange(e){
  const target = e.target;
  const value = target.type === 'checkbox' ? target.checked : target.value;
  const name = target.name;
  this.setState(
    {
      [name]: value
    });
}

handleChange(e){
   
  const value = e.target.value;
  console.log(value);
  this.setState(
    {
      fileId :value
    });
    console.log(value);
    let param={fileId:value}
    window.post('checkFile',param,function(data){
      if(!data.success){
       $("[name=userId]").val("文件已经存在！");
      return;
      }
  
      console.log(data);
   
    });
    
  
}
onTest(){
  console.log(this.state.departments)
  var selectedBank='';
		var banksObj = this.state.departments;
		for(var key in banksObj){
			if(banksObj[key]){
				selectedBank+=key+',';
			}
		}
    selectedBank = selectedBank.substr(0,selectedBank.length-1);
    console.log(selectedBank)
}
render() {
return (

  <div>
    <h1>上传文件</h1>  
    <Header/>
         <Celer/>
  <div className = 'uploadbg'>

  
   <div className="upload" >
   
   <form  encType="multipart/form-data" ref='uploadForm'>
  
      <table className='tab'>
			<tbody>
          <tr>
          <td>
             <div className='tb-title'>文件编号</div>
             <input type="text" name="fileId" id="fileId" required="required" placeholder="文件编号" onBlur={this.handleChange}></input>
           </td>
        </tr>
        <tr>
            <td style={{marginTop:'20px'}}>
            <div className='tb-title'>文件</div> 
            <input type="file" required="required" placeholder="file" id="upfile" name="file" ref='uploadFrom2'></input>
            </td>
        </tr>
  
           <tr>
            <td style={{marginTop:'20px'}}>
            <div className='tb-title'>所属部门</div> 
          	
            <div className="checkpart">
            <div><input ref='departments' type='checkbox' name='departments' value="2" className='checkbox' onChange={this.InputChange}/><span>部门1</span></div>
            <div><input ref='departments' type='checkbox' name='departments' value="3" className='checkbox' onChange={this.InputChange}/><span>部门2</span></div>
            <div><input ref='departments' type='checkbox' name='departments' value="4" className='checkbox' onChange={this.InputChange}/><span>部门3</span></div>
            <div><input ref='departments' type='checkbox' name='departments' value="1" className='checkbox' onChange={this.InputChange}/><span>部门4</span></div>
            {/* <button onClick={this.onTest.bind(this)}>test</button> */}
           </div>
           </td>
        </tr>

			 <tr >
            <td style={{marginTop:'20px'}}>
					      <div className='tb-title'>军衔</div> 
                <select name='allowRank' className='user-input2' onChange={this.handleInputChange} >
                    <option value ="1"> 上将</option>
                    <option value ="2"> 中将</option>
                    <option value ="3"> 少将</option>
                    <option value ="4"> 大校</option>
                    <option value ="5"> 上校</option>
                    <option value ="6"> 中校</option>
                    <option value ="7"> 少校</option>
                    <option value ="8"> 上尉</option>
                    <option value ="9"> 中尉</option>
                    <option value ="10"> 少尉</option>
                </select>
			
			    	</td>
			  </tr>
        <tr>
          <td>
            <div className='tb-title'>文件描述</div>
            <input type="text" name="description" id="description" required="required" placeholder="文件描述" onChange={this.handleInputChange}></input>
          </td>
        </tr>
      </tbody>
		  </table>
      
        <button className="btn btn-primary btn-block btn-large"    onClick={this.uploadFile2.bind(this)}>上传</button>
        </form> 
         </div>
      
        </div>
         </div>
        
    );
}
}

export default UploadFile;