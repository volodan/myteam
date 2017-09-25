const divStyle = {
  float: 'left',
  display: 'inline',
  margin: '5px',
};

var MonthYearPicker = React.createClass({

  render: function () {
    var rowMonths = [];
    
    for (var i = 1; i < 13; i++) {
      rowMonths.push(<li id={i} onClick={this.handleClickMonth.bind(null, i)}><a href='#'>{i}</a></li>);
    }

    var rowYears = [];
    var actDate = new Date();

    for (var j = actDate.getFullYear() - 7; j < actDate.getFullYear() + 3; j++) {
      rowYears.push(<li id={j} onClick={this.handleClickYear.bind(null, j)}><a href='#'>{j}</a></li>);
      
    }

    return (
      <div className="panel panel-default">

        <div className="panel-heading" ><h2>Positions</h2></div>
        
        <div className="dropdown" style={divStyle}>
          <button id="month" className="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Selected month:&nbsp;
                <span id="selectedMonthColumn" className="selected">{this.state.selectedMonth}</span></button>
          <ul className="dropdown-menu">
            {rowMonths}
          </ul>
        </div>

        <div className="dropdown" style={divStyle}>
          <button id="year" className="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Selected year:&nbsp;
                <span id="selectedYearColumn" className="selected">{this.state.selectedYear}</span></button>
          <ul className="dropdown-menu">
            {rowYears}
          </ul>
        </div>


        <PositionTable positions={this.state.positions} />
      </div>
    );
  },

  handleClickMonth: function (text) {
    this.state.selectedMonth = text;
    console.log("selected month: " + this.state.selectedMonth);
    this.loadPositionsFromServer();
  },

  handleClickYear: function (text) {
    this.state.selectedYear = text;
    console.log("selected month: " + this.state.selectedYear);
    this.loadPositionsFromServer();
  },

  componentDidMount: function () {
    this.loadPositionsFromServer();
  },

  getInitialState: function () {
    var actDate = new Date();
    return { selectedMonth: actDate.getMonth() + 1, selectedYear: actDate.getFullYear() ,positions: [] };
  },

  loadPositionsFromServer: function () {
    var self = this;
    var positionDataUrl = "/api/position/all/" + this.state.selectedMonth + "/" + this.state.selectedYear;
    console.log("url: " + positionDataUrl);
    $.ajax({
      url: positionDataUrl
    }).then(function (data) {
      self.setState({ positions: data });
    });
  },
});


var LoadData = React.createClass({



  getInitialState: function () {
    return { positions: [] };
  },

  componentDidMount: function () {
    this.loadPositionsFromServer();
  },

  render() {
    return (<PositionTable positions={this.state.positions} />);
  }
});

var Position = React.createClass({
  render: function () {
    return (
      <tr>
        <td>{this.props.position.email}</td>
        <td>{this.props.position.firstname}</td>
        <td>{this.props.position.lastname}</td>
        <td>{this.props.position.joinDate}</td>
        <td>{this.props.position.billable ? "YES" : "NO"}</td>
        <td>{this.props.position.billablePercentage}</td>
        <td>{this.props.position.contractor ? "YES" : "NO"}</td>
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
  render: function () {
    var rows = [];
    this.props.positions.forEach(function (position) {
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

ReactDOM.render(<MonthYearPicker />, document.getElementById('picker'));
