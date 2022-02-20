import React, { useEffect, useState } from 'react';
import './index.css';
import Editor from "../../components/elements/Editor";

const URL = 'https://server.idiomcentric.com'

interface Chunk {
  id: string;
  title: string;
  body: string;
  createdAt: Date;
  updatedAt: Date
}

function App() {
  const EDITOR_CHUNK = 'editor-chunk'

  const defaultChunk: Chunk = {
    id: '201051ff-e936-4f5e-9cc4-364240f0af30',
    title: 'title',
    body: `# First go

example list

- [ ] one
- [ ] two
- [ ] three
- [ ] four
- [ ] five
`,
    createdAt: new Date('2022-02-13T16:53:42.947Z'),
    updatedAt: new Date('2022-02-13T16:53:42.947Z')
  }


  const [readOnly, setReadOnly] = useState(true);

  const [chunk, setChunk] = useState<Chunk>(initialChunk());
  const [chunks, setChunks] = useState(initialChunks());

  useEffect(() => { window.localStorage.setItem(EDITOR_CHUNK, JSON.stringify(chunk)) }, [chunk])
  useEffect(() => {
    fetch(`${URL}/api/chunks`)
      .then(response => response.json())
      .then(returnedChunks => setChunks(returnedChunks));
  }, [chunk]);

  const onCreate = () => {
    return fetch(`${URL}/api/chunks`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: 'new: ' + JSON.stringify(new Date()), body: '#new' })
    }).then(response => response.json())
  }

  const onDelete = () => {
    return fetch(`${URL}/api/chunks/${chunk.id}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
    }).then(_ => setChunk(chunks[0]))
  }

  const onSave = () => {
    return fetch(`${URL}/api/chunks/${chunk.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(chunk)
    })
  }

  return (
    <div className="wrapper">
      <div className="chunks">
        <h1>Chunks</h1>
        <ul>{chunks.map(ch => <li onClick={() => { setChunk(ch) }} key={ch.id}>{ch.title}</li>)}</ul>
      </div>
      <div className="editor">
        <div className='title'><h1>{chunk.title}</h1></div>
        <Editor
          defaultValue={chunk.body}
          currentValue={chunk.body}
          readOnly={readOnly}
          onChange={(currentChunkBody: () => any) => {
            setChunk({
              ...chunk, 
              body: currentChunkBody()
            })
          }}
        />
        <div className='control'>
          <button onClick={() => { onCreate().then(chunk => setChunk(chunk)).then() }}>New</button>
          <button onClick={() => { onSave() }}>Save</button>
          <button onClick={() => { onDelete() }}>Delete</button>
          <button onClick={() => { setReadOnly(!readOnly) }}>{ readOnly?'Edit':'Read' }</button>
        </div>
      </div>
    </div>
  )

  function initialChunk(): Chunk {
    const storageChunk = window.localStorage.getItem(EDITOR_CHUNK)
    if (storageChunk) {
      return JSON.parse(storageChunk)
    } else {
      return defaultChunk
    }
  }

  function initialChunks(): Chunk[] {
    return []
  }

}

export default App;
