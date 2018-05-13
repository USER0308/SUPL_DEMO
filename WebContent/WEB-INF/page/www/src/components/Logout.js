import React from 'react';
class Logout extends React.Component {


  componentDidMount(){

    var  sessionId=sessionStorage.getItem("sessionId");
   alert(sessionId);
    var param={sessionId:sessionId};
    window.get('logout',param,function(data){
    sessionStorage.removeItem('userId');
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('sessionId');
    sessionStorage.clear();
  
    })
    window.location.href='/login';
  }


  render() {
    return (<div></div>);
  }
}


export default Logout;
