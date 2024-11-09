import React, { useState } from 'react';
import './FileUploader.css';

const FileUploader = () => {
  const [file, setFile] = useState(null);

  // Funkcija za postavljanje odabrane datoteke
  function handleFileChange(e) {
    setFile(e.target.files[0]);
  };

  return (
    <div className="file-uploader">
      <h2>Upload your file</h2>
      <input 
        type="file" 
        onChange={handleFileChange} 
        className="file-input"
      />
      {file && (
        <div className="file-info">
          <p><strong>File Name:</strong> {file.name}</p>
          <p><strong>File Size:</strong> {(file.size / 1024).toFixed(2)} KB</p>
        </div>
      )}
    </div>
  );
};

export default FileUploader
