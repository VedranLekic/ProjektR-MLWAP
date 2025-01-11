import React, { useState } from 'react';
import './FileUploader.css';

const FileUploader = () => {
  const [file, setFile] = useState(null);
  const [selectedDataType, setSelectedDataType] = useState('');
  const [selectedFramework, setSelectedFramework] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  // Function to handle file selection
  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  // Function to handle data type selection
  const handleDataTypeChange = (e) => {
    setSelectedDataType(e.target.value);
  };

  // Function to handle framework selection
  const handleFrameworkChange = (e) => {
    setSelectedFramework(e.target.value);
  };

  // Function to handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!file || !selectedDataType || !selectedFramework) {
      setError('Please fill out all fields.');
      return;
    }

    setIsLoading(true);
    setError('');

    // Create a FormData object to send the file and metadata
    const formData = new FormData();
    formData.append('file', file);
    formData.append('dataType', selectedDataType);
    formData.append('desiredFramework', selectedFramework);

    try {
      // Send the data to the backend
      const response = await fetch('http://localhost:8080/api/upload', {
        method: 'POST',
        body: formData,
      });

      if (!response.ok) {
        throw new Error('Failed to upload file.');
      }

      const result = await response.text();
      alert(result); // Show success message from the backend
    } catch (err) {
      setError(err.message || 'An error occurred while uploading the file.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="file-uploader">
      <h2>Upload your file</h2>
      <form onSubmit={handleSubmit}>
        <input 
          type="file" 
          onChange={handleFileChange} 
          className="file-input"
          required
        />
        <div className="dropdown-container">
          <label>
            Data type:
            <select value={selectedDataType} onChange={handleDataTypeChange} required>
              <option value="">Select data type</option>
              <option value="CSV">CSV</option>
              <option value="Excel">Excel</option>
              <option value="TXT">TXT</option>
              <option value="JSON">JSON</option>
            </select>
          </label>
          <label>
            Desired framework:
            <select value={selectedFramework} onChange={handleFrameworkChange} required>
              <option value="">Select framework</option>
              <option value="PyTorch">PyTorch</option>
              <option value="TensorFlow">TensorFlow</option>
              <option value="JAX">JAX</option>
              <option value="Flax">Flax</option>
            </select>
          </label>
        </div>
        <button type="submit" className="submit-button" disabled={isLoading}>
          {isLoading ? 'Processing...' : 'Format File'}
        </button>
        {error && <p className="error-message">{error}</p>}
      </form>
      {file && (
        <div className="file-info">
          <p><strong>File Name:</strong> {file.name}</p>
          <p><strong>File Size:</strong> {(file.size / 1024).toFixed(2)} KB</p>
        </div>
      )}
    </div>
  );
};

export default FileUploader;