import React, { Component } from 'react'
import { Panel, Button, Col, Row, Modal, Navbar, Nav, NavItem } from 'react-bootstrap'
import { Field, reduxForm } from 'redux-form'
import { bindActionCreators } from 'redux'
import { registerUser, addUserAddress,getUsersByRole } from '../actions/index';

import { connect } from 'react-redux';
import RowMenu from './row_main'
class MainMenu extends Component {
    constructor(props, context) {
        super(props, context);

        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);

        this.state = {
            show: false,
            panel_title: "Seh",
            type: "Neighbor"
        }
    }
    handleClose() {
        this.setState({ show: false });
    }

    handleShow() {
        //console.log(this.state.login.data)
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
        console.log(values)
        this.state.type == "Neighbors" && this.props.addUserAddress(values, () => {
            this.handleClose()
        }) ||
        this.state.type == "Employees" && this.props.registerUser(values, () => {
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
            </Navbar>
        )
    }
    render() {
        const { handleSubmit } = this.props
        console.log(this.props.login)
        return (
            <div>
                {this.loadNavBar()}
                <Modal show={this.state.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Modal heading</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {/* TODO: Move modal form to a separate class */}
                        <form className="form-page" onSubmit={handleSubmit(this.onSubmit.bind(this))}>
                            {this.state.type == " Neighbors" &&
                                <Field
                                    label="Address"
                                    name="address"
                                    component={this.renderField} /> ||
                                this.state.type == "Employees" &&
                                <div>
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
                                </div>

                            }

                            <button type="submit" className="btn_N"> Add Address </button>

                        </form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.handleClose}>Close</Button>
                    </Modal.Footer>
                </Modal>
                <Panel>
                    <Panel.Heading>
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
    console.log(state)

    return {
        login: state.login
    };
}

function mapDispatchToProps(dispatch) {

    return bindActionCreators({ login }, dispatch);
}

export default reduxForm({
    validate,
    form: "AddForm"
})(connect(mapStateToProps, { registerUser, addUserAddress,getUsersByRole })(MainMenu))
