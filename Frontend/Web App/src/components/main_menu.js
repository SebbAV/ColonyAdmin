import React, { Component } from 'react'
import { Panel, Button, Col, Row, Modal, Navbar, Nav, NavItem } from 'react-bootstrap'
import { Field, reduxForm } from 'redux-form'
import { Formik, Form, Field as FormikField, ErrorMessage } from 'formik'
import { bindActionCreators } from 'redux'
import { registerUser, addUserAddress, getUsersByRole, createVisit, loadAddress } from '../actions/index';

import { connect } from 'react-redux';
import RowMenu from './row_main'
const colors = [{ color: 'Red', value: 'ff0000' },
{ color: 'Green', value: '00ff00' },
{ color: 'Blue', value: '0000ff' }]
class MainMenu extends Component {
    constructor(props, context) {
        super(props, context);

        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);

        this.state = {
            show: false,
            panel_title: "Seh",
            type: "Neighbors"
        }
    }
    componentDidMount() {
        this.props.loadAddress();
    }
    handleClose() {
        this.setState({ show: false });
    }
    handleShow() {
        this.state.type == "Employees" && this.props.getUsersByRole()
        this.setState({ show: true });
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
    onSubmit(values) {
        this.state.type == "Neighbors" && this.props.addUserAddress(values, () => {
            this.handleClose()
        }) ||
            this.state.type == "Employees" && this.props.registerUser(values, () => {
                this.handleClose()
            }) ||
            this.state.type == "Guests" && this.props.createVisit(values, () => {
                this.handleClose()
            })
    }
    loadNavBar() {
        return (
            <Navbar inverse>
                <Navbar.Header>
                    <Navbar.Brand>
                        <a href="#brand">COLONY Admin</a>
                    </Navbar.Brand>
                    <Navbar.Toggle />
                </Navbar.Header>
                <Navbar.Collapse>
                    <Nav>
                        <NavItem onClick={() => this.setState({ type: "Employees" })} href="#">
                            Employees
                        </NavItem>
                        <NavItem onClick={() => this.setState({ type: "Neighbors" })} href="#">
                            Neighbors
                        </NavItem>
                        <NavItem onClick={() => this.setState({ type: "Guests" })} href="#">
                            Guests
                        </NavItem>
                        <NavItem onClick={() => this.setState({ type: "SOS" })} href="#">
                            SOS
                        </NavItem>
                    </Nav>
                </Navbar.Collapse>
            </Navbar >
        )
    }
    loadOptions() {
        console.log("--------------------------------------")
        console.log(this.props.address)
        if (this.props.address.addresses) {
            return _.map(this.props.address.addresses.data, address => {
                return (<option value={address._id} key={address._id}>{address.address}</option>)
            })
        }
        else {
            return <option>Empty</option>
        }

    }
    render() {
        const { handleSubmit } = this.props
        console.log("renders")
        console.log(this.state.type)
        return (
            <div >
                {this.loadNavBar()}
                <Modal show={this.state.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Modal heading</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {/* TODO: Move modal form to a separate class */}
                        {/* <form className="form-page" onSubmit={handleSubmit(this.onSubmit.bind(this))}> */}
                        {this.state.type == "Neighbors" &&
                            <div>
                                <Formik initialValues={{ first_name: '', last_name: '', vehicle: '', address: '', address_number: '' }}
                                    onSubmit={(values, { setSubmitting }) => {
                                        setTimeout(() => {
                                            alert(JSON.stringify(values, null, 2));
                                            setSubmitting(false);
                                            this.props.registerUser(values)
                                        }, 400);
                                    }}>
                                    {({
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
                                                <label htmlFor="password" style={{ display: 'block' }}>
                                                    Password
                                                </label>
                                                <FormikField name="password" className="text-input" />
                                                <button type="submit" disabled={isSubmitting} style={{ display: 'block' }} className="text-input">
                                                    Submit
                                                </button>
                                            </Form>
                                        )}
                                </Formik>
                            </div> ||
                            this.state.type == "Employees" &&
                            <div>
                                <Formik initialValues={{ first_name: '', last_name: '', vehicle: '', address: '', address_number: '' }}
                                    onSubmit={(values, { setSubmitting }) => {
                                        setTimeout(() => {
                                            alert(JSON.stringify(values, null, 2));
                                            setSubmitting(false);
                                            this.props.registerUser(values)
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
                                                <label htmlFor="password" style={{ display: 'block' }}>
                                                    Password
                                    </label>
                                                <FormikField name="password" className="text-input" />
                                                <button type="submit" disabled={isSubmitting} style={{ display: 'block' }} className="text-input">
                                                    Submit
                                    </button>
                                            </Form>
                                        )}
                                </Formik>
                            </div>
                            ||
                            this.state.type == "Guests" &&
                            <div>
                                <Formik initialValues={{ first_name: '', last_name: '', vehicle: '', address: '', address_number: '' }}
                                    onSubmit={(values, { setSubmitting }) => {
                                        setTimeout(() => {
                                            alert(JSON.stringify(values, null, 2));
                                            setSubmitting(false);
                                            this.props.createVisit(values)
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
                                                <label htmlFor="first_name" style={{ display: 'block' }}>
                                                    First Name
                                                </label>
                                                <FormikField name="first_name" className="text-input" />
                                                <label htmlFor="last_name" style={{ display: 'block' }}>
                                                    Last Name
                                                </label>
                                                <FormikField name="last_name" className="text-input" />
                                                <label htmlFor="vehicle" style={{ display: 'block' }}>
                                                    Vehicle
                                                </label>
                                                <FormikField name="vehicle" className="text-input" />
                                                <label htmlFor="address" style={{ display: 'block' }}>
                                                    Address
                                                </label>
                                                <FormikField component="select" name="address" className="text-input" >
                                                    {this.loadOptions()}
                                                </FormikField>
                                                <label htmlFor="address_number" style={{ display: 'block' }}>
                                                    Address Number
                                                </label>
                                                <FormikField name="address_number" className="text-input" />
                                                <button type="submit" disabled={isSubmitting} className="text-input" >
                                                    Submit
                                                     </button>
                                            </Form>
                                        )}
                                </Formik>
                            </div>
                        }

                        {/* <button type="submit" className="btn_N"> Add Address </button> */}

                        {/* </form> */}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.handleClose}>Close</Button>
                    </Modal.Footer>
                </Modal>
                <Panel className="margin-panel" bsClass="panel-color">
                    <Panel.Heading >
                        <Row>
                            <Col md={8}>
                                <h3>{this.state.type ? this.state.type : 'Default Title'}</h3>
                            </Col>
                            <Col md={4}>
                                <Button onClick={this.handleShow}>Add</Button>
                            </Col>
                        </Row>
                    </Panel.Heading>
                    <Panel.Body>
                        <RowMenu type={this.state.type} />
                    </Panel.Body>
                </Panel>
            </div >
        )

    }
}
function validate(values) {
    const errors = {};

    return errors;
}
function mapStateToProps(state) {
    return {
        login: state.login,
        address: state.addresses
    };
}

function mapDispatchToProps(dispatch) {

    return bindActionCreators({ login }, dispatch);
}

export default reduxForm({
    validate,
    form: "AddForm"
})(connect(mapStateToProps, { registerUser, addUserAddress, getUsersByRole, createVisit, loadAddress })(MainMenu))
