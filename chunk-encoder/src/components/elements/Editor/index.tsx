// @ts-ignore
import Editor from 'rich-markdown-editor'
import { Box, chakra } from '@chakra-ui/react'
import './index.css'

const CEditor = chakra(Editor)

interface MDEditorProps {
    initValue: string;
    readOnly: boolean;
    onChange: (val: () => void) => void;
}

const MDEditor: React.FC<MDEditorProps> = ({
    initValue,
    readOnly,
    onChange
}) => {
    return (
        <Box pos={'relative'} bg={'white'}>
            { /* @ts-ignore */ }
            <CEditor
                className='editor'
                data-testid='editor'
                value={initValue}
                readOnly={readOnly}
                readOnlyWriteCheckboxes={true}
                onChange={onChange}
            />
        </Box>
    )
}

export default MDEditor
