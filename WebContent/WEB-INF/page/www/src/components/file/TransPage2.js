require('normalize.css/normalize.css');
require('styles/App.css');
import React from 'react';
import ReactDOM from 'react-dom';
import Header from '../Header';
import Celer from '../Celer';
var usertype = sessionStorage.getItem('usertype');
var username=sessionStorage.getItem("orgname");

class TransPage2 extends React.Component {
  constructor(){
    super();
    this.state = {
        conID:'',
        saleOrg:'',
        buyOrg:'',
        transType:'1',
        amount:'',
        latestStatus:''
    }

 this.handleInputChange = this.handleInputChange.bind(this);
  }

componentWillMount() {
  sessionStorage.setItem("pageInt",3);
}


 uploadFile() {  

    var uploadForm = ReactDOM.findDOMNode(this.refs.upload);
    console.log(this.refs.upload);
    var formData = new FormData(uploadForm);

    formData.append('cid',this.state.conID);   
    formData.append('compa',this.state.saleOrg);
    formData.append('compb',username);
    formData.append('type',this.state.transType);
    formData.append('limit',this.state.amount);
    formData.append('latestStatus',this.state.latestStatus);
 
    console.log(formData);
        alert(0);
    window.formPost('uploadFile',formData,function(data){
        if(!data.success){
            alert(data.message);
        }
    });
     
    

    window.location.href='/trans/list';
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

render() {
       
return (
  
  <div>
    <h1>交易录入</h1>  
    <Header/>
         <Celer/>
  <div className = 'uploadbg'>
   <div className = 'transpage'>
        
    <form encType="multipart/form-data" ref="upload">
        <div>
            <input type="text" required="required" placeholder="conID"  name="conID"  onChange={this.handleInputChange}></input>
            <input type="text" required="required" placeholder="saleOrg"  name="saleOrg"  onChange={this.handleInputChange}></input>
            <input type="text" required="required"   value={"A"} disabled></input>
            <select type="text" required="required" placeholder="transType"  name="transType"  onChange={this.handleInputChange}>
                <option value ="1"  onChange={this.handleChange}>原料购买</option>
                  <option value ="2" onChange={this.handleChange}>成品购买</option>
                  <option value ="3" onChange={this.handleChange}>其他购买</option></select>
            <input type="number" required="required" placeholder="amount"  name="amount"  onChange={this.handleInputChange}></input>
            
            <select type="text" required="required" placeholder="latestStatus"  name="latestStatus"  onChange={this.handleInputChange}>
                <option value ="U"  onChange={this.handleChange}>未入库</option>
                <option value ="C" onChange={this.handleChange}>已入库</option>
            </select>            
            <input type="file" required="required" placeholder="file"  name="file" ref='uploadForm'></input>
        </div>
        <button className="btn btn-primary btn-block btn-large"    onClick={this.uploadFile.bind(this)}>上传</button>
    </form>

      
         </div>
        
         </div>
         </div>
    );
}
}



export default TransPage2;