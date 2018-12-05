import React, { Component } from 'react'
import socketIOClient from 'socket.io-client'
import { API_URL } from '../actions/index'
import { Panel, Row, Col } from 'react-bootstrap'

const socket = socketIOClient(API_URL)
class SosTable extends Component {
    constructor() {
        super();
        this.state = {
            response: []
        }
    }
    componentDidMount() {

        socket.on("sos_request", data => this.setState({ response: [...this.state.response, data] }));

    }

    closeSocketSession(address){
        console.log(address)
        socket.emit("sos_finish",address)
    }
    createSOSPanel() {
        return _.map(this.state.response, sos => {
            return (
                <div key={sos.address} onClick={this.closeSocketSession.bind(this,sos.address)}>
                    <Panel bsClass="panel-color">
                        <Panel.Body>
                            <Row>
                                <Col xs={8} md={10}>
                                    <h3 id="lblName">
                                        {sos.address ? sos.address: 'Address'}
                                    </h3>
                                    <label id="lblAddress">
                                        {sos.address_number? sos.address_number : 'Address Number'}
                                    </label>
                                </Col>
                            </Row>
                        </Panel.Body>
                    </Panel>
                </div>
            )
        })

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