require('normalize.css/normalize.css');
require('styles/App.css');
import React from 'react';
import ReactDOM from 'react-dom';
import Header from '../Header';
import Celer from '../Celer';


var token=sessionStorage.getItem("token");
var  sessionId=sessionStorage.getItem("sessionId");

class AddUser extends React.Component {
  constructor(){
    super();

    this.state = {
        name:'',
        rank:1,
        department:1
        //rank和department都是select框，得先赋值1，否则不触发onChange方法，导致无值
    }

     this.handleInputChange = this.handleInputChange.bind(this);
     this.handleSubmit = this.handleSubmit.bind(this);
     this.handleChange = this.handleChange.bind(this);
     //每一个使state变动的方法都必须指定this，即加上后续的.bind(this)
  }

componentWillMount() {
  sessionStorage.setItem("pageInt",1);
}

handleSubmit() {  
  
    var param = {userId:this.state.userId,userName:this.state.name,password:this.state.password,rank:this.state.rank,department:this.state.department,token:token,sessionId:sessionId};
    //thie.state.xx是查询条目中输入框的值，前面的item是后台搜寻用的条目名
    //post方法空格里填写后台方法的url     
    window.post('addUser',param,function(data){
     
       console.log("增加用户成功！");
        
          
        window.location.href='/file/add';
        // 成功后跳转页面
    }); 
          
}
  
handleChange(e){
    const value = e.target.value;
    console.log(value);
    this.setState(
      {
        userId :value
      });
      console.log(value);
      var param={userId:value,token:token,sessionId:sessionId};
      window.post('checkUser',param,function(data){
        if(!data.success){
         $("[name=userId]").val("请重新输入！");
        return;
        }
    
        console.log(data);
     
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

render() {
       
return (
  
  <div>
    <h1>创建用户</h1>  
    <Header/>
    <Celer/>
  <div className = 'uploadbg'>
  <div className = 'upload'>
  <table className='tab'>
			<tbody>
        <tr>
            <td style={{marginTop:'20px'}}>
                <div className='tb-title'>证件号</div>
                <input type="text" required="required"   name="userId" onBlur={this.handleChange}></input>
            </td>	
          
        </tr>
        <tr>
            <td style={{marginTop:'20px'}}>
                <div className='tb-title'>用户名</div>
                <input type="text" required="required"   name="name" onChange={this.handleInputChange}></input>
            </td>	
          
        </tr>
        <tr>
            <td style={{marginTop:'20px'}}>
                <div className='tb-title'>密 码</div>
                <input type="password" required="required"   name="password" onChange={this.handleInputChange}></input>
            </td>	
          
        </tr>


        <tr >
            <td style={{marginTop:'20px'}}>
					      <div className='tb-title'>所属部门</div> 
                <select name='department' className='user-input2' onChange={this.handleInputChange} >
                    <option value ="1"> 司令部</option>
                    <option value ="2"> 政治部</option>
                    <option value ="3"> 后勤部</option>
                    <option value ="4"> 装备部</option>
                </select>
			
			    	</td>
			  </tr>

			 <tr >
            <td style={{marginTop:'20px'}}>
					      <div className='tb-title'>军衔</div> 
                <select name='rank' className='user-input2' onChange={this.handleInputChange} >
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
			</tbody>
		</table>
   
  
        <button className="btn btn-primary btn-block btn-large"   onClick={this.handleSubmit} >增加</button>
         </div>
        
         </div>
         </div>
    );
}
}
export default AddUser;