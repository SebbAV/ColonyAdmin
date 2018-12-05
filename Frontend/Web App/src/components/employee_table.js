import React, { Component } from 'react'
import EmployeesTable from './employee_table'
import 'redux'
import { reduxForm, Field } from 'redux-form'
import { connect } from 'react-redux';
import { getUsersByRole, sample,updateUser } from '../actions/index';
import { Formik, Form, Field as FormikField, ErrorMessage } from 'formik'
import { Panel, Button, Col, Row, Modal, Navbar, Nav, NavItem } from 'react-bootstrap'
const data = {
    // used to populate "account" reducer when "Load" is clicked
    first_name: 'Jane',
    last_name: 'Jane'
}
class EmployeeTable extends Component {
    constructor() {
        super()

        this.state = {
            show: false
        }
        this.handleClose = this.handleClose.bind(this)
    }
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
    handleClose() {
        this.setState({ show: false });
    }
    showEditModal(user) {
        console.log(user)
        if (user) {
            this.setState({ show: true,temp_user: user });
        }
        if (this.state.show) {
            let user = this.state.temp_user
            return (

                <div>
                    <Formik initialValues={{ _id:user._id,role:user.role,email: user.email, first_name: user.first_name, last_name: user.last_name, phone: user.phone }}
                        onSubmit={(values, { setSubmitting }) => {
                            setTimeout(() => {
                                alert(JSON.stringify(values, null, 2));
                                setSubmitting(false);
                                this.props.updateUser(values,() => {
                                    this.setState({show:false})
                                    this.props.getUsersByRole()
                                })
                                
                                
                            }, 400);
                        }}>

                        {({
                            values,
                            handleChange,
                            handleBlur,
                            handleSubmit,
                            isSubmitting,
                        }) => (
                                <Form>
                                    <label htmlFor="email" style={{ display: 'block' }}>
                                        Email
                                </label>
                                    <FormikField name="email" className="text-input" />

                                    <label htmlFor="first_name" style={{ display: 'block' }}>
                                        First Name
                                </label>
                                    <FormikField name="first_name" className="text-input" />
                                    <label htmlFor="last_name" style={{ display: 'block' }}>
                                        Last Name
                                </label>
                                    <FormikField name="last_name" className="text-input" />
                                    <label htmlFor="phone" style={{ display: 'block' }}>
                                        Phone
                                </label>
                                    <FormikField name="phone" className="text-input" />
                                    <button type="submit" disabled={isSubmitting} style={{ display: 'block' }} className="text-input">
                                        Submit
                                </button>
                                </Form>
                            )}
                    </Formik>
                </div>

            )
        }
    }
    handleClick(user, i) {
        console.log("Seh")
        console.log(user)
        this.showEditModal(user)
    }
    loadUsers() {

        if (this.props.users.users_role) {
            let { users_role } = this.props.users
            return _.map(users_role.data, user => {
   
                if (!user) {
                    return (<h3>No users found </h3>)
                }
                else {
                    return (
                        <div key={user._id} onClick={this.handleClick.bind(this, user)}>
                            <Row>

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

    }
    render() {
        console.log("First table render")
        const { handleSubmit, load } = this.props
        console.log(this.state.show)
        return (
            <div>
                <Modal show={this.state.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Modal heading</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {this.showEditModal()}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.handleClose}>Close</Button>
                    </Modal.Footer>
                </Modal>
                {this.loadUsers()}
            </div >
        )
    }

}
function mapStateToProps(state) {
    return {
        users: state.users,
        initialValues: state.prefill
    };
}

export default reduxForm({
    form: "EditForm"
})(connect(mapStateToProps, { getUsersByRole, load: sample,updateUser })(EmployeeTable))