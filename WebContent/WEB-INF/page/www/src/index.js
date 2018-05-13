import 'core-js/fn/object/assign';
import React from 'react';
import ReactDOM from 'react-dom';
import { Router, IndexRoute, Route, browserHistory} from 'react-router';
import Login from './components/Login';
import Logout from './components/Logout';

import AddUser from './components/file/AddUser';
import QueryFile from './components/file/QueryFile';
import UploadFile from './components/file/UploadFile';
import TransPage from './components/file/TransPage';
import TransPage2 from './components/file/TransPage2';

// Render the main component into the dom

ReactDOM.render((
<Router history={browserHistory}>

  <Route path="login" component={Login}/>
 
  <Route path="logout" component={Logout}/>

<Route path="file">
    <IndexRoute component={AddUser}/>
    <Route path="add" component={AddUser}/>
    <Route path="query" component={QueryFile}/> 
    <Route path="upload" component={UploadFile}/>
    <Route path="TransPage" component={TransPage}/>
    <Route path="TransPage2" component={TransPage2}/>
  
</Route>

 

</Router>
), document.getElementById('app'))

