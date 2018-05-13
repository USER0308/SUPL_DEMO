import React from 'react';
import ReactDOM from 'react-dom';
import { Link } from 'react-router';


class Celer extends React.Component {
	
  
render() {  
	var pageInt=sessionStorage.getItem('pageInt');
	var usertype=sessionStorage.getItem('usertype');
	return (
    <div>
      <aside>
		<nav className="tower-sidebar">
				
			<div>
						<ul className={ pageInt == 1 ? 'current' : 'none'}>
    						<Link to='/file/add' className='done'>
							<span className="tower-sidebar-item">创建用户</span></Link>&nbsp;
      				 	</ul>
			 			<ul className={ pageInt == 2 ? 'current' : 'none'}>
                			<Link to='/file/upload' className='done'>
							<span className="tower-sidebar-item">上传文件</span></Link>&nbsp;
       					</ul>   					
						
						<ul className={ pageInt == 3 ? 'current' : 'none'}>         
							<Link to='/file/query' className='done'>
							<span className="tower-sidebar-item">查询文件</span></Link>
						</ul>
						<ul className={ pageInt == 4 ? 'current' : 'none'}>         
							<Link to='/file/TransPage2' className='done'>
							<span className="tower-sidebar-item">测试</span></Link>
						</ul>
			</div>
		
	
		
		</nav>
	</aside>
    </div>
    );
  }
}

export default Celer;