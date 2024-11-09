import React from 'react';
import './App.css';
import Header from './components/Header.jsx';
import FileUploader from './components/FileUploader.jsx';

const App = () => {
  return (
    <div className="App">
      <Header />
      <main className="main-content">
        <FileUploader />
      </main>
    </div>
  );
};

export default App;
