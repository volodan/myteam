var MonthYearPicker = React.createClass({
	render: function() {
		
	var rows = [];
    for (var i = 1; i < 13; i++) {
        rows.push(<li id={i} onClick={this.handleClick.bind(null, i)}><a href='#'>{i}</a></li>);
    } 
		
    return (
    		<div className="dropdown">
    		  <button id="month" className="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Dropdown Example
    		  <span className="selectd"></span></button>
    		  <ul className="dropdown-menu">
    		  	{rows}
    		  </ul>  
    		</div>

    );
  },

  handleClick: function(text) {
	  alert(text);
  },
	  

 /*
  handleSubmit(e) {
    e.preventDefault();
    var newItem = {
      text: this.state.text,
      id: Date.now()
    };
    this.setState((prevState) => ({
      items: prevState.items.concat(newItem),
      text: ''
    }));
  }*/
});





var LoadData = React.createClass({
 
  loadPositionsFromServer: function () {
    var self = this;
    $.ajax({
      url: "http://localhost:8080/api/position/all/10/2017"
    }).then(function (data) {
      self.setState({positions: data});
    });
  },
 
  getInitialState: function () {
    return {positions: []};
  },
 
  componentDidMount: function () {
    this.loadPositionsFromServer();
  },
 
  render() {
    return ( <PositionTable positions={this.state.positions}/> );
  }
});

var Position = React.createClass({
   render: function() {
     return (
       <tr>
         <td>{this.props.position.email}</td>
         <td>{this.props.position.firstname}</td>
         <td>{this.props.position.lastname}</td>
         <td>{this.props.position.joinDate}</td>
         <td>{this.props.position.billable ? "YES" : "NO" }</td>
         <td>{this.props.position.billablePercentage}</td>
         <td>{this.props.position.contractor ? "YES" : "NO" }</td>
         <td>{this.props.position.rate}</td>
         <td>{this.props.position.salary}</td>
         <td>{this.props.position.cogs}</td>
         <td>{Math.round(this.props.position.gm * 100) / 100}</td>
         <td>{this.props.position.jobRole}</td>
         <td>{this.props.position.project}</td>
       </tr>);
   }
 });

var PositionTable = React.createClass({
   render: function() {
     var rows = [];
     this.props.positions.forEach(function(position) {
       rows.push(<Position position={position} />);
     });
     return (
       <table className="table table-striped">
         <thead>
           <tr>
             <th>Email</th>
             <th>First name</th>
             <th>Last Name</th>
             <th>Join Date</th>
             <th>Billable</th>
             <th>Bill. Perc.</th>
             <th>Contractor</th>
             <th>Rate</th>
             <th>Salary</th>
             <th>COGS</th>
             <th>GM</th>
             <th>Job Role</th>
             <th>Project</th>
           </tr>
         </thead>
         <tbody>{rows}</tbody>
       </table>);
   }
 });
 
ReactDOM.render(<LoadData />, document.getElementById('data') );
ReactDOM.render(<MonthYearPicker />, document.getElementById('picker') );

