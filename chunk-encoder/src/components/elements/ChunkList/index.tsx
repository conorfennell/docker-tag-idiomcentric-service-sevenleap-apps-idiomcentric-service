import { List, ListItem } from '@chakra-ui/react'
import { Chunk } from '../../../types'

interface ChunkListProps {
    chunks: Chunk[];
    selected: string;
    onClick: (chunk: Chunk) => void;
}

const ChunkList: React.FC<ChunkListProps> = ({ chunks, selected, onClick }) => {
    return (
        <List pl={6}>
            {chunks.map(chunk => (
                <ListItem
                    key={chunk.id}
                    onClick={() => onClick(chunk)}
                    cursor={'pointer'}
                    mb={2}
                    p={2}
                    boxSizing={'border-box'}
                    _hover={{ bg: 'gray.500', color: 'white' }}
                    sx={{
                        bg: selected === chunk.id ? 'green' : 'white',
                        color: selected === chunk.id ? 'white' : 'black',
                    }}
                >
                    {chunk.title}
                </ListItem>
            ))}
        </List>
    )
}

export default ChunkList
