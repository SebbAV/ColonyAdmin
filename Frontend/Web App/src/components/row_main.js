import React, { Component } from 'react'
import { Panel, Row, Col, Modal, Button } from 'react-bootstrap'
import EmployeesTable from './employee_table'
import VisitorsTable from './visitors_table'
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form'
import { getUserGroupedByAddress, getUsersByRole } from '../actions/index';
import SosTable from './sos_table'
import _ from 'lodash'

const data = {

}

class RowMain extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {
            show: false
        }
        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }
    guestMenu() {
        return (<VisitorsTable />)
    }
    componentDidMount() {
        this.props.getUserGroupedByAddress()
    }
    editUser(user_id) {

    }
    handleClose() {
        this.setState({ show: false });
    }

    handleShow() {

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
    loadUsers(users) {
        if (users) {
            return _.map(users, user => {

                if (!user) {
                    return (<h3>No users found </h3>)
                }
                else {
                    return (
                        <div key={user._id} >
                            <Row>

                                <Col xs={9} md={10}>
                                    <label id="lblName">
                                        {user.first_name} {user.last_name}
                                    </label>
                                </Col>
                                <Col xs={9} md={10}>
                                    <label id="lblAddress">
                                        Address : {user.address_number}
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
    loadNeighborPanels() {
        if (this.props.users.users) {
            return _.map(this.props.users.users.data, address => {
                if (!address) {
                    return (<h3>No users found </h3>)
                }
                if (address.users.length == 0) {

                }
                else {
                    return (
                        <div key={address._id} >
                            <Panel bsClass="panel-color">
                                <Panel.Heading>
                                    <h2>{address.address}</h2>
                                </Panel.Heading>
                                <Panel.Body>
                                    {this.loadUsers(address.users)}
                                </Panel.Body>
                            </Panel>
                        </div>
                    )
                }
            })

        }
        else {
            return <h4> Loading... </h4>
        }
    }
    neighborMenu() {
        return (
            <div className="margin-panel">
                {this.loadNeighborPanels()}
            </div >
        )
    }

    employeesMenu() {
        return <EmployeesTable />
    }
    sosMenu() {
       return <SosTable/>
        // return (
        //     <div>
        //         <Panel>
        //             <Panel.Body>
        //                 <Row>
        //                     <Col xs={8} md={10}>
        //                         <h3 id="lblName">
        //                             {this.props.Name ? this.props.Name : 'Name'}
        //                         </h3>
        //                         <label id="lblAddress">
        //                             {this.props.Address ? this.props.Name : 'Address'}
        //                         </label>
        //                     </Col>
        //                     <Col xs={8} md={10}>
        //                         <label id="lblDescription">
        //                             {this.props.Code ? this.props.Name : 'Description'}
        //                         </label>
        //                     </Col>
        //                 </Row>
        //             </Panel.Body>
        //         </Panel>
        //     </div>
        // )
    }
    defaultMenu() {
        return (
            <div>
                <Panel>
                    <Panel.Body>
                        <Row>
                            <Col xs={8} md={10}>
                                <h3 id="lblName">
                                    {this.props.Name ? this.props.Name : 'Name'}
                                </h3>
                                <label id="lblAddress">
                                    {this.props.Address ? this.props.Name : 'Address'}
                                </label>
                            </Col>
                            <Col xs={4} md={2}>
                                <input type="button" text="Edit" onClick={this.Edit} />
                            </Col>
                            <Col xs={8} md={10}>
                                <label id="lblCode">
                                    {this.props.Code ? this.props.Name : 'Code'}
                                </label>
                            </Col>
                            <Col xs={4} md={2}>
                                <input type="button" text="Delete" onClick={this.Delete} />
                            </Col>
                        </Row>
                    </Panel.Body>
                </Panel>
            </div>
        )
    }
    onSubmit(values) {
        values["role"] = "4"

        this.props.registerUser(values, () => {
            this.handleClose()
        })
    }
    render() {
        const { handleSubmit } = this.props

        return (
            <div>
                <Modal show={this.state.show} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Modal heading</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {/* TODO: Move modal form to a separate class */}
                        <form className="form-page" onSubmit={handleSubmit(this.onSubmit.bind(this))}>
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
                                label="Address"
                                name="address"
                                component={this.renderField} />
                            <Field
                                label="Phone"
                                name="phone"
                                component={this.renderField} />
                            <button type="submit" className="btn_N"> Sign up </button>

                        </form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={this.handleClose}>Close</Button>
                    </Modal.Footer>
                </Modal>
                {(this.props.type == 'Guests') && this.guestMenu() ||
                    (this.props.type == 'Neighbors') && this.neighborMenu() ||
                    (this.props.type == 'Employees') && this.employeesMenu() ||
                    (this.props.type == 'SOS') && this.sosMenu() ||
                    this.neighborMenu()}
            </div>
        )
    }
}
function mapStateToProps(state) {


    return {
        users: state.users
    };
}
function validate(values) {
    const errors = {};

    return errors;
}
export default reduxForm({
    validate,
    form: "modularForm"
})(connect(mapStateToProps, { getUsersByRole, getUserGroupedByAddress })(RowMain))