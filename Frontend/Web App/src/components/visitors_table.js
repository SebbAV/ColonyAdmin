import React, {Component} from 'react'
import { Panel, Row, Col, Modal, Button } from 'react-bootstrap'
import EmployeesTable from './employee_table'
import { connect } from 'react-redux';

class VisitorsTable extends Component {
    componentDidMount() {
    }
    loadUsers() {

        if (this.props.users.users_role) {
            let { users_role } = this.props.users
            return _.map(users_role.data, user => {
                console.log(user)
                if (!user) {
                    return (<h3>No users found </h3>)
                }
                else {
                    return (
                        <div key={user._id} >
                            <Row>
                                {console.log(user)}
                                <Col xs={9} md={10}>
                                    <label id="lblName">
                                        {user.first_name} {user.last_name}
                                    </label>
                                </Col>
                                <Col xs={9} md={10}>
                                    <label id="lblAddress">
                                        Phone Number : {user.phone}
                                    </label>
                                </Col>
                                <Col xs={9} md={10}>
                                    <label id="lblAddress">
                                        Email : {user.email}
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
             {/* {this.loadUsers()} */}
            </div>
        )
    }

}
function mapStateToProps(state) {
    return {
        users: state.users
    };
}
export default connect(mapStateToProps, null)(VisitorsTable)