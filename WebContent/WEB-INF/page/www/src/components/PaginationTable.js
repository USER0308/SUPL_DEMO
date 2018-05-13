import React,{Component} from 'react';
import PageComponent from './pageComponent';
import '../styles/App.css';
class PaginationTable extends Component{
    
    constructor(props){
        super(props);
        this.state = {
            indexList : [], //获取数据的存放数组
            totalNum:'',//总记录数
            totalData:{},
            current: 1, //当前页码
            pageSize:5, //每页显示的条数10条
            goValue:'',
            totalPage:''//总页数
        }
    }

    componentWillMount(){
         var _this = this;
         var data = _this.props.data;
         _this.setState({totalData:data})
         _this.setState({totalNum:data.length})
         //计算总页数= 总记录数 / 每页显示的条数
         let totalPage =Math.ceil( data.length / _this.state.pageSize);
         _this.setState({totalPage:totalPage},()=>{
             _this.pageClick(1);
         })
    }
      componentWillReceiveProps(nextProps){
        var _this = this;
        var data = nextProps.data;
        console.log('接收data为',data)
        if(_this.props.data ===  nextProps.data)
        {return false}
        if(data === undefined){
           this.setState(
            {
              data:[]
            });
        }
          //计算总页数= 总记录数 / 每页显示的条数
        let totalPage =Math.ceil( data.length / _this.state.pageSize);
        _this.setState({totalData:data,
            totalNum:data.length,
            totalPage:totalPage},()=>{
              _this.pageClick(1);
            });
        }
  
   //点击翻页
   pageClick(pageNum){
            let _this = this;
           if(pageNum != _this.state.current){
               _this.state.current = pageNum
           }
           
           _this.state.indexList=[];//清空之前的数据
           for(var i = (pageNum - 1) * _this.state.pageSize; i< _this.state.pageSize * pageNum; i++){
               if(_this.state.totalData[i]){
                   _this.state.indexList.push(_this.state.totalData[i])
               }
           }
           _this.setState({indexList:_this.state.indexList})
    }
    //上一步
    goPrevClick(){
        var _this = this;
        let cur = this.state.current;
        if(cur > 1){
            _this.pageClick( cur - 1);
        }
    }
    //下一步
    goNext(){
        var _this = this;
        let cur = _this.state.current;
        //alert(cur+'==='+_this.state.totalPage)
        if(cur < _this.state.totalPage){
            _this.pageClick(cur + 1);
        }
    }
    //跳转到指定页
    goSwitchChange(){
            var _this=this;
            var value = _this.props.goValue;
            //alert(value+'==='+_this.state.totalPage)
            if(!/^[1-9]\d*$/.test(value)){
                alert('页码只能输入大于1的正整数');
            }else if(parseInt(value) > parseInt(_this.state.totalPage)){
                alert('没有这么多页');
            }else{
                _this.pageClick(value);
            }
            _this.setState({goValue :''})

    }
 //输入框赋值
    pageValue = (e) => {
          var _this = this;
          _this.setState({goValue : e.target.value})
          let value = e.target.value;
          alert(value+'==='+_this.props.totalPage);
        }

  makeTable(props) {

    let spanClass = {  
        backgroundColor:'red',  
        color:'white'
    };  
  //  var proxy = new Proxy(props, {
  //   get: function(target, property) {
  //     if (property in target) {
  //       return target[property];
  //     } else {
  //       throw new ReferenceError('Property \'' + property + '\' does not exist.');
  //     }
  //   }
  // });

  //let data = proxy.data;
  let data = this.state.indexList;
  let columns = props.columns;
  let header  = props.header;
  let extraColumns =  props.extraColumns;
  let extraColumnMap =  props.extraColumnMap;
  const trItems = data.map((element,index) =>{
    if(element.Record !== undefined){
      element = element['Record'];
    }

    var tdItems =[];
      if(columns !== undefined){

        for(var col of columns){
         for (var key in element) {
            if(key.toUpperCase() == col.toUpperCase()){
//console.log("key:",key," col:",col)
              var item = element[key];
              tdItems.push(<td key={'td'+key+index}> {item} </td>);
            }
        }
      }

    //for extra column map
      if(extraColumnMap !== undefined ){
        for (var key in element) {
            for(var ele  of extraColumnMap){
                if( undefined !== ele[key+'.' + element[key]]){
                     let exCols = ele[key+'.' + element[key]];
                     tdItems.push(<td key={'extd'+index} >
                     {(() => {
                         var displayContent = [];
                         for(var exKey in exCols){
                            var exCol = exCols[exKey];
                            switch (exCol.type) {
                                case 'img': displayContent.push( <img src={exCol.props.src} data-index={index}  key={'img'+exKey+index}
                                data-record={JSON.stringify(element)}  onClick={this.props.onClick} />);
                                    break;
            
                                case 'button': displayContent.push(<button  className='btn btn-default' key={'btn'+exKey+index} data-value={exCol.props.value}
                                      data-index={index}   data-record={JSON.stringify(element)} onClick={this.props.onClick}>
                                    {exCol.props.children}</button>);
                                    break;
            
                                default : displayContent.push(<span className='tdSpan'  key={'span'+exKey+index}>{exCol} </span>);
                              }
                           }
                           return displayContent;
                         })()}
                     </td>);
                  }
            }
           
        }
    }

      //for extra columns
      if(extraColumns !== undefined){
        for(var exCol of extraColumns){
            if(this.props.onClick != undefined){
              tdItems.push(<td key={'extd'+index} >
              {(() => {switch (exCol.type) {
                    case 'img': return <img src={exCol.props.src} data-index={index}
                    data-record={JSON.stringify(element)}  onClick={this.props.onClick} />

                    case 'button': return <button  className='btn btn-default'
                          data-index={index}   data-record={JSON.stringify(element)} onClick={this.props.onClick}>
                        {exCol.props.children}</button>

                    default : return <span className='tdSpan' onClick={this.props.onClick} >{exCol} </span>
                  }})()}
              </td>);
            }else{
              tdItems.push(<td key={'extd'+index} >
              {(() => {switch (exCol.type) {
                    case 'img': return <img src={exCol.props.src}  data-index={index} data-record={JSON.stringify(element)}  />

                    case 'button': return <button className='btn btn-default'
                          data-index={index}   data-record={JSON.stringify(element)}>{exCol.props.children}</button>

                    default : return <span  className='tdSpan'>{exCol} </span>
                  }})()}
              </td>);
            }
        }
      }
    }else{
        for (var key in element) {
        if(element.hasOwnProperty(key)){
          var item = element[key];
          tdItems.push(<td key={'td'+key+index}> {item} </td>);
        }
      }
    }

     return( <tr key={index}>
          {tdItems}
      </tr>)
   }
  );


  if(header !== undefined) {

    let thItems =[];
    let i = 0;
      for(var item of header){
          thItems.push(<th key={'th' + i++}> {item} </th>);
      }

     return (
      <div className='paDiv'>
     <div className='showTableDiv'>
       <table className='showTable'>
        <tbody>
         <tr>{thItems}</tr>
         {trItems}
        </tbody>
      </table>
      </div>
       <PageComponent total={this.state.totalNum}
                          current={this.state.current}
                          totalPage={this.state.totalPage}
                          goValue={this.state.goValue}
                          pageClick={this.pageClick.bind(this)}
                          goPrev={this.goPrevClick.bind(this)}
                          goNext={this.goNext.bind(this)}
                          switchChange={this.goSwitchChange.bind(this)} />
      </div>);
  }else{
     return (
     <div className='paDiv'>
     <div className='showTableDiv'>
       <table className='showTable'>
        <tbody>
         {trItems}
        </tbody>
      </table>
      </div>
       <PageComponent total={this.state.totalNum}
                          current={this.state.current}
                          totalPage={this.state.totalPage}
                          goValue={this.state.goValue}
                          pageClick={this.pageClick.bind(this)}
                          goPrev={this.goPrevClick.bind(this)}
                          goNext={this.goNext.bind(this)}
                          switchChange={this.goSwitchChange.bind(this)} />
  </div>
  );
  }
}

    render(){
      return (
        this.makeTable(this.props)
      );
    }
}
export default PaginationTable;