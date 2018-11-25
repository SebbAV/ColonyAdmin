import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form'
import { Panel, Button } from 'react-bootstrap'
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { loginUser } from '../actions/index';
import './index.css'


class Login extends Component {
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
    signUp() {
        this.props.history.push('');
    }
    onSubmit(values) {

        this.props.loginUser(values, () => {
            this.props.history.push('/main')
        })
    }
    render() {
        const { handleSubmit } = this.props
        return (
            <div className="containe center-panel">
                <Panel className="panel-color">
                    <Panel.Body>
                        <form onSubmit={handleSubmit(this.onSubmit.bind(this))}>
                            <Field
                                label="Username"
                                name="email"
                                className="login"
                                component={this.renderField} />
                            <Field
                                label="Password"
                                className="login"
                                name="password"
                                component={this.passwordfield} />
                            <button type="submit" className="btn_N" block> Login </button><br />
                            <p align="center"><Link to="/forgot_password"> Forgot Password? Click here </Link></p>
                            <p align="center"><Link to="/register">Not registered? Sign up </Link></p>
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

function mapStateToProps(state) {
    console.log(state)
    return {
        login: state.login
    };
}
export default reduxForm({
    validate,
    form: "LoginForm"
})(connect(mapStateToProps, { loginUser })(Login));