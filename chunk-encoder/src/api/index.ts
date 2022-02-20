import { URL } from '../config'
import { Chunk } from '../types'

export const onCreate = () => {
    return fetch(`${URL}/api/chunks`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: 'new: ' + JSON.stringify(new Date()), body: '#new' })
    }).then(response => response.json())
  }

export const onDelete = (id: string) => {
    return fetch(`${URL}/api/chunks/${id}`, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
    })
}

export const onSave = (chunk: Chunk) => {
    return fetch(`${URL}/api/chunks/${chunk.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(chunk)
    })
}

export const getChunks = () => {
    return fetch(`${URL}/api/chunks`)
      .then(response => response.json())
}
