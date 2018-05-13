import React from 'react';
import { Link } from 'react-router';



class Header extends React.Component {  
  render() {      
    var username = sessionStorage.getItem('userId');
    return (
    <div>
      	 <nav className="tower-navigation">
				<div className="tower-logo-container">
           <span className="tower-logo2 ">用户:{username}</span>
				 <Link to='/logout' className='headNav'>退出</Link> 
				</div>
			</nav> 


    </div>
    );
  }
}

export default Header;
