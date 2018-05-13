require('normalize.css/normalize.css');
require('styles/App.css');
import React from 'react';
class Login extends React.Component {

  constructor(){
    super();
    this.state = {
      password:'',
      userId:''
    }
  }

//  handleSubmit() {
//   let values = this.state;
//   let param={userId:values.userId,password:values.password}
//   window.postlogin('login',param,function(data){
//     if(!data.success){
//       alert(data.message);
//     return;
//     }

//     sessionStorage.setItem('userId',values.userId);
//     sessionStorage.setItem('token',data.obj);
//     console.log(data.message);
//     console.log(data.obj);
//     alert(data.obj);
//     window.location.href='/file/add';
//   });
// }

handleSubmit() {
  let values = this.state;
  let param={userId:values.userId,password:values.password}
  window.postlogin('login',param,function(data){
  if(!data.success){
 
  return;
  }
  
  var data2 = JSON.parse(data.obj);
  sessionStorage.setItem('sessionId',data2.sessionId);
  sessionStorage.setItem('token',data2.token);
  sessionStorage.setItem('userId',values.userId);
  window.location.href='/file/add';
  });
  }

handleInputChange(e){
  const target = e.target;
  const value = target.type === 'checkbox' ? target.checked : target.value;
  const name = target.name;
  this.setState(
    {
      [name]: value,
    });
}

render() {
       
return (

      <div className="login">
	    <h2>Login</h2>

		  <input type="text" name="userId" placeholder="user ID" required="required" onChange={this.handleInputChange.bind(this)}/>
		  <input type="password" name="password" placeholder="password" required="required" onChange={this.handleInputChange.bind(this)} />
		  <button className="btn btn-primary btn-block btn-large" onClick ={this.handleSubmit.bind(this)} >login</button>
      </div>
    );
}
}
export default Login;