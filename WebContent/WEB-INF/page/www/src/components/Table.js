import React,{Component} from 'react';
class Table extends Component{
  constructor(){
      super();
  }


  makeTable(props) {

   var proxy = new Proxy(props, {
    get: function(target, property) {
      if (property in target) {
        return target[property];
      } else {
        throw new ReferenceError('Property \'' + property + '\' does not exist.');
      }
    }
  });

  let data = proxy.data;
  let columns = props.columns;
  let header  = props.header;
  let extraColomns =  props.extraColomns;
  let RecKey = props.RecKey;

  const trItems = data.map((element,index) =>{
    if(element.Record !== undefined){
      element = element.Record;
    }
    var tdItems =[];
      if(columns !== undefined){

        for(var col of columns){
         for (var key in element) {
            if(key.toUpperCase() == col.toUpperCase()){
              var item = element[col];
              tdItems.push(<td key={'td'+key+index}> {item} </td>);
            }
        }
      }
      //for extra columns
      if(extraColomns !== undefined){
        for(var exCol of extraColomns){
            if(this.props.onClick != undefined){
              if(RecKey !== undefined){
                tdItems.push(<td key={'extd'+index} >
                  { exCol.type =='img' ? <img src={exCol.props.src} alt={element[RecKey] +'index'+index} onClick={this.props.onClick} />
                : <span  onClick={this.props.onClick} >{exCol} </span>}  </td>);
              }
            }else{
              tdItems.push(<td key={'extd'+index}> {exCol} </td>);
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
     <div>
       <table className='showTable'>
        <tbody>
         <tr>{thItems}</tr>
         {trItems}
        </tbody>
      </table>
      </div>);
  }else{
     return (<div><table className='showTable'>
        <tbody>
         {trItems}
        </tbody>
      </table></div>);
  }

}

    render(){
      return (
        this.makeTable(this.props)
      );
    }
}
export default Table;