import { useState } from 'react';
import axios from 'axios';

export default function KnowledgeUpload({ token }) {
  const [file, setFile] = useState(null);
  const [status, setStatus] = useState('');

  const uploadFile = async () => {
    if (!file) return;
    setStatus('Uploading...');
    const formData = new FormData();
    formData.append('file', file);

    try {
      await axios.post('/api/knowledge/upload', formData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'multipart/form-data',
        },
      });
      setStatus('Upload successful!');
    } catch (err) {
      setStatus('Upload failed.');
    }
  };

  return (
    <div className="p-4">
      <input type="file" onChange={(e) => setFile(e.target.files[0])} />
      <button onClick={uploadFile} className="bg-blue-500 text-white p-2 rounded ml-2">
        Upload
      </button>
      <div>{status}</div>
    </div>
  );
}
