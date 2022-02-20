import React, { useEffect, useState } from 'react';
import './index.css';
import Editor from '../../components/elements/Editor'
import { onCreate, onDelete, onSave, getChunks } from '../../api'
import { Chunk } from '../../types'

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
  useEffect(() => { getChunks().then(returnedChunks => setChunks(returnedChunks)) }, [chunk]);

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
          <button onClick={() => { onSave(chunk) }}>Save</button>
          <button onClick={() => { onDelete(chunk.id).then(_ => setChunk(chunks[0])) }}>Delete</button>
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
