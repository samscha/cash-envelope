import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

const styles = StyleSheet.create({
  container: {
    borderColor: 'blue',
    borderWidth: 1,
    height: 100,
    margin: 20,
    // width: 100,
  },
});

const Envelope = _ => (
  <View style={styles.container}>
    <Text style={styles.title}>asdf</Text>
  </View>
);

Envelope.navigationOptions = {
  title: 'Envelope',
};

export default Envelope;
