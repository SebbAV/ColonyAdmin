import React, { Component } from 'react'
import { Panel, Row, Col, Modal, Button } from 'react-bootstrap'
import EmployeesTable from './employee_table'
import { loadVisitors,exitVisitors } from '../actions/index'
import { connect } from 'react-redux';

class VisitorsTable extends Component {
    componentDidMount() {
        this.props.loadVisitors()
    }
    handleClick(key,event){
        console.log(key)
        let obj = {'vehicle':key}
        {window.confirm("Do you want close the active session?") && this.props.exitVisitors(obj)}
        
    }
    loadUsers() {
        console.log(this.props.visitors.visitors)
        if (this.props.visitors.visitors) {
            return _.map(this.props.visitors.visitors.data, user => {
                console.log(user)
                if (!user) {
                    return (<h3>No users found </h3>)
                }
                else if (user.exit_date) {

                }
                else {
                    return (
                        <div key={user.vehicle} onClick={this.handleClick.bind(this,user.vehicle)} >
                            <Row>
                                {console.log(user)}
                                <Col xs={9} md={10}>
                                    <label id="lblName">
                                        {user.address}
                                    </label>
                                </Col>
                                <Col xs={9} md={10}>
                                    <label id="lblAddress">
                                        {user.entrance_date}
                                    </label>
                                </Col>
                                <Col xs={9} md={10}>
                                    <label id="lblAddress">
                                        {user.vehicle}
                                    </label>
                                </Col>
                            </Row>
                            <hr></hr>
                        </div>
                    )
                }
            })
        }
        else {
            return <h4> Loading... </h4>
        }
    }
    render() {
        console.log("First table render")

        return (
            <div>
                {this.loadUsers()}
            </div>
        )
    }

}
function mapStateToProps(state) {
    return {
        visitors: state.visitors
    };
}
export default connect(mapStateToProps, { loadVisitors,exitVisitors })(VisitorsTable)