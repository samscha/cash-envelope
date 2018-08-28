'use strict';

// tag::vars[]
const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
// end::vars[]

// tag::app[]
class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = { envelopes: [] };
  }

  componentDidMount() {
    client({ method: 'GET', path: '/api/envelopes' }).done(response => {
      this.setState({ envelopes: response.entity._embedded.envelopes });
    });
  }

  render() {
    return <EnvelopeList envelopes={this.state.envelopes} />;
  }
}
// end::app[]

// tag::employee-list[]
class EnvelopeList extends React.Component {
  render() {
    var envelopes = this.props.envelopes.map(envelope => (
      <Envelope key={envelope._links.self.href} envelope={envelope} />
    ));
    return (
      <table>
        <tbody>
          <tr>
            <th>Envelope Name</th>
            <th>Envelope Value</th>
            {/* <th>Description</th> */}
          </tr>
          {envelopes}
        </tbody>
      </table>
    );
  }
}
// end::employee-list[]

// tag::employee[]
class Envelope extends React.Component {
  render() {
    return (
      <tr>
        <td>{this.props.envelope.envelopeName}</td>
        <td>{this.props.envelope.envelopeValue}</td>
        {/* <td>{this.props.envelope.description}</td> */}
      </tr>
    );
  }
}
// end::employee[]

// tag::render[]
ReactDOM.render(<App />, document.getElementById('react'));
// end::render[]
