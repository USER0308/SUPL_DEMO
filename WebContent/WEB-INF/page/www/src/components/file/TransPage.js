require('normalize.css/normalize.css');
require('styles/App.css');
import React from 'react';
import ReactDOM from 'react-dom';
import Header from '../Header';
import Celer from '../Celer';
var usertype = sessionStorage.getItem('usertype');
var username=sessionStorage.getItem("orgname");

class TransPage extends React.Component {
  constructor(){
    super();
    this.state = {
        fileId:'',
        allowRank:'1',
        description:''
    }

 this.handleInputChange = this.handleInputChange.bind(this);
  }

componentWillMount() {
  sessionStorage.setItem("pageInt",3);
}


 uploadFile() {  

    var uploadForm = ReactDOM.findDOMNode(this.refs.upload);
    console.log(this.refs);
    var formData = new FormData(uploadForm);
    console.log(uploadForm);
    formData.append('fileId',this.state.fileId);   
    formData.append('allowRank',this.state.allowRank);
    formData.append('description',this.state.description);
    formData.append("userId","12345");
    formData.append("department","2");
    formData.append("allowDep","1,2");
    console.log(formData);
    alert(this.state.fileId);
    alert(this.state.description);

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
    <h1>文件上传</h1>  
    <Header/>
         <Celer/>
  <div className = 'uploadbg'>
   <div className = 'transpage'>
        
    <form encType="multipart/form-data" ref="upload">
        <div>

            <input type="text" required="required" placeholder="文件编号"  name="fileId"  onChange={this.handleInputChange}></input>
            {/* <input type="text" required="required" placeholder="saleOrg"  name="saleOrg"  onChange={this.handleInputChange}></input> */}
           
            <select type="text" required="required" placeholder="级别"  name="allowRank"  onChange={this.handleInputChange}>
                <option value ="1"  onChange={this.handleChange}>上将</option>
                  <option value ="2" onChange={this.handleChange}>中将</option>
                  <option value ="3" onChange={this.handleChange}>。。。</option></select>
           
            <input type="text" required="required" placeholder="description"  name="description"  onChange={this.handleInputChange}></input>   
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



export default TransPage;