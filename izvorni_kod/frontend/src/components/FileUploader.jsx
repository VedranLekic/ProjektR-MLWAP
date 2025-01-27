import React, { useState } from "react";
import "./FileUploader.css";

const FileUploader = () => {
  const [file, setFile] = useState(null);
  const [selectedDataType, setSelectedDataType] = useState("");
  const [selectedFramework, setSelectedFramework] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const [processedFileName, setProcessedFileName] = useState("");
  const [userNumber, setUserNumber] = useState("");

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleDataTypeChange = (e) => {
    setSelectedDataType(e.target.value);
  };

  const handleFrameworkChange = (e) => {
    setSelectedFramework(e.target.value);
  };

  const handleNumberChange = (e) => {
    setUserNumber(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!file || !selectedDataType || !selectedFramework) {
      setError("Please fill out all fields.");
      return;
    }

    setIsLoading(true);
    setError("");

    const formData = new FormData();
    formData.append("file", file);
    formData.append("dataType", selectedDataType);
    formData.append("desiredFramework", selectedFramework);
    formData.append("userNumber", userNumber);

    try {
      const response = await fetch("http://localhost:8080/api/upload", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        throw new Error("Failed to process file.");
      }

      const result = await response.json();
      setProcessedFileName(result.fileName);
      alert(result.message);
    } catch (err) {
      setError(err.message || "An error occurred while uploading the file.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleDownload = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/download/${processedFileName}`);
      if (!response.ok) {
        throw new Error("Failed to download file.");
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = processedFileName;
      a.click();
    } catch (err) {
      setError("An error occurred while downloading the file.");
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
    <div className="input-container">
      <label htmlFor="userNumber">Enter a number:</label>
        <input
          id="userNumber"
          type="number"
          value={userNumber}
          onChange={handleNumberChange}
          className="number-input"
          required
        />
      </div>
        <div className="dropdown-container">
          <label>
            Data type:
            <select
              value={selectedDataType}
              onChange={handleDataTypeChange}
              required
            >
              <option value="">Select data type</option>
              <option value="CSV">CSV</option>
              <option value="Excel">Excel</option>
              <option value="TXT">TXT</option>
              <option value="JSON">JSON</option>
            </select>
          </label>
          <label>
            Desired framework:
            <select
              value={selectedFramework}
              onChange={handleFrameworkChange}
              required
            >
              <option value="">Select framework</option>
              <option value="PyTorch">PyTorch</option>
              <option value="TensorFlow">TensorFlow</option>
              <option value="JAX">JAX</option>
              <option value="Flax">Flax</option>
            </select>
          </label>
        </div>
        <button type="submit" className="submit-button" disabled={isLoading}>
          {isLoading ? "Processing..." : "Format File"}
        </button>
        {error && <p className="error-message">{error}</p>}
      </form>
      {processedFileName && (
        <button onClick={handleDownload} className="download-button">
          Download Processed File
        </button>
      )}
    </div>
  );
};

export default FileUploader;
