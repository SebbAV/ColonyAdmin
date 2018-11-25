import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form'
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { registerUser } from '../actions/index';
import { Button, Panel, ButtonGroup } from 'react-bootstrap'


class Register extends Component {
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
        values["role"] = "4"
        this.props.registerUser(values, () => {
            this.props.history.push('/');
        })
    }
    render() {
        const { handleSubmit } = this.props
        return (
            <div className="container center-panel-2">
                <Panel className="panel-color">
                    <Panel.Body>
                        <form className="form-page" onSubmit={handleSubmit(this.onSubmit.bind(this))}>
                            <p align="left"><Link to="/" className="btn btn-default btn-xs">X</Link></p>
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
                            <button type="submit" className="btn_N" block> Sign up </button>

                        </form>
                    </Panel.Body>
                </Panel>
            </div>
        )
    }
}
function validate(values) {
    const errors = {};

    return errors;
}


export default reduxForm({
    validate,
    form: "RegisterForm"
})(connect(null, { registerUser })(Register));