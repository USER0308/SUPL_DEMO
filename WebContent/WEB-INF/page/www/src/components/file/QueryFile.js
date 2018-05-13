require('normalize.css/normalize.css');
require('styles/App.css');
import React from 'react';
import ReactDOM from 'react-dom';
import Header from '../Header';
import Celer from '../Celer';
import PaginationTable from '../PaginationTable';

import {browserHistory} from 'react-router';
import $ from 'jquery';



var token=sessionStorage.getItem("token");
var  sessionId=sessionStorage.getItem("sessionId");

class QueryFile extends React.Component {
  constructor(){
    super();
    this.state = {
      data:[]
      //先赋值data为空，否则可能出现table里的data无传值找不到致页面报错的现象
    }
     this.handleQuery = this.handleQuery.bind(this);
     this.handleInputChange = this.handleInputChange.bind(this);
     //每一个使state变动的方法都必须指定this，即加上后续的.bind(this)
  }

componentWillMount() {
    sessionStorage.setItem("pageInt",1);
     //post方法空格里填写后台方法的url   
    var _this = this;
     //指定_this为this，特指下面的data赋值情况
    
     var param = {userId:'00001',pageNum:'1',pageSize:'1000',token:token,sessionId:sessionId};
     window.post('queryAllFile',param,function(data){
    
       console.log(data.message);  
       console.log(data.obj);  
      console.log(data.obj.list);
    
    //  //根据后台返回的data传值给分页table的data，根据情况转为字符串
       _this.setState({
        data: data.obj.list
        });             
    }); 
  
    
}

handleQuery() {  
    var param = {userId:'zj111',department:this.state.item,pageNum:this.props.pageNum,pageSize:'5',token:token,sessionId:sessionId};
    //thie.state.item是查询条目中输入框的值，前面的item是后台搜寻用的条目名
    var _this = this;
    //指定_this为this，特指下面的data赋值情况
    //post方法空格里填写后台方法的url     
    window.post('queryAllFile',param,function(data){
     //var list = JSON.parse(data)
     console.log(data.message);  
     console.log(data.obj);  
     alert(0);
     //根据后台返回的data传值给分页table的data，根据情况转为字符串
      _this.setState({
        data: data.obj.list
      });             
    }); 
          
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



handleDown(e){
  var target = e.target;
  console.log(target);
  var record = JSON.parse(target.dataset.record);
  alert(record.fileId);
  var param = {fileId:record.fileId,token:token,sessionId:sessionId};
 
  window.post('dowloadFile',param,function(data){
    alert("下载成功");
    console.log(data.message);         
   }); 



}



render() {
return (

      
        <div id = 'Allfile'>
        <h1>查询文件</h1>
        <div className='translist'>
            <div className='subtitle'>部门编号</div>
            <input type='text' name='item' className='user-input2' onChange={this.handleInputChange} />
            <button onClick={this.handleQuery}>查询</button>
        </div>
        <Header/>
        <Celer/> 
        <PaginationTable data ={this.state.data}  onClick={this.handleDown.bind(this)}
    columns={['fileId','description','userId','department','allowRank']} 
    extraColumns={[<button>下载</button>]}
        header = {['文件编号','文件描述','上传人ID','文件所属部门','等级','操作']} /> 
        {/* colunms里填写data里对应显示的key，header里填写要显示的项目名称 */}
         </div>
);
}
}
export default QueryFile;