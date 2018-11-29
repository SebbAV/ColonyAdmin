import React, { Component } from 'react'
import { Panel, Row, Col, Modal, Button } from 'react-bootstrap'
import EmployeesTable from './employee_table'
import 'redux'
import { reduxForm, Field } from 'redux-form'
import { connect } from 'react-redux';
import { getUsersByRole,sample } from '../actions/index';
const data = {
    // used to populate "account" reducer when "Load" is clicked
    first_name: 'Jane',
    last_name: 'Jane'
}
class EmployeeTable extends Component {
    componentDidMount() {
        this.props.getUsersByRole()
    }
    renderField(field) {
        return (
            <div className="form-group">
                <label>{field.label}</label>
                <input
                    className="form-control"
                    type="text"
                    {...field.input}
                />
            </div>
        )
    }
    passwordfield(field) {
        return (
            <div className="form-group">
                <label>{field.label}</label>
                <input
                    className="form-control"
                    type="password"
                    {...field.input}
                />
            </div>
        )
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
    onSubmit(values) {
        console.log(values)
        console.log("Printing props")
        console.log(this.props)
    }
    render() {
        console.log("First table render")
        const { handleSubmit, load } = this.props
        return (
            <div>
                {/* {this.loadUsers()} */}

                <div className="container center-panel-2">
                    <Panel className="panel-color">
                        <Panel.Body>
                            <form className="form-page" onSubmit={handleSubmit(this.onSubmit.bind(this))}>
                                <button type="button" onClick={() => load(data)}>Load Account</button>
                                <p align="left"></p>
                                <Field
                                    label="Email"
                                    name="email"
                                    component={this.renderField} />
                                <Field
                                    label="First Name"
                                    name="first_name"
                                    component={this.renderField} />
                                <Field
                                    label="Last Name"
                                    name="last_name"
                                    component={this.renderField} />
                                <Field
                                    label="Phone"
                                    name="phone"
                                    component={this.renderField} />
                                <Field
                                    label="Password"
                                    name="password"
                                    component={this.passwordfield} />
                                <Field
                                    label="Confirm password"
                                    name="pwd-confirm"
                                    component={this.passwordfield} />
                                <button type="submit" className="btn_N"> Sign up </button>

                            </form>
                        </Panel.Body>
                    </Panel>
                </div>
            </div>
        )
    }

}
function mapStateToProps(state) {
    console.log("prestate")
    console.log(state)
    return {
        users: state.users,
        initialValues: state.prefill
    };
}

export default reduxForm({
    form: "EditForm"
})(connect(mapStateToProps, { getUsersByRole, load: sample })(EmployeeTable))