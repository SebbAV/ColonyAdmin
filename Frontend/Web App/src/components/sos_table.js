import React, { Component } from 'react'
import socketIOClient from 'socket.io-client'
import { API_URL } from '../actions/index'
import {Panel,Row,Col} from 'react-bootstrap'

class SosTable extends Component {
    constructor() {
        super();
        this.state = {
            response: []
        }
    }
    componentDidMount() {
        const socket = socketIOClient(API_URL)
        socket.on("sos_request", data => this.setState({ response: [...this.state.response,data] }));
    }
    createSOSPanel() {
        return (
            <div>
                <Panel bsClass="panel-color">
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
                            <Col xs={8} md={10}>
                                <label id="lblDescription">
                                    {this.props.Code ? this.props.Name : 'Description'}
                                </label>
                            </Col>
                        </Row>
                    </Panel.Body>
                </Panel>
            </div>
        )
    }
    render() {
        console.log(this.state.response)
        return (
            <div>
                {this.createSOSPanel()}
            </div>
        )
    }

}

export default SosTable;