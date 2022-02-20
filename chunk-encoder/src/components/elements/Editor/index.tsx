// @ts-ignore
import Editor from 'rich-markdown-editor'
import { Box } from '@chakra-ui/react'
import './index.css'

interface MDEditorProps {
    defaultValue: string;
    currentValue: string;
    readOnly: boolean;
    onChange: (val: () => void) => void;
}

const MDEditor: React.FC<MDEditorProps> = ({
    defaultValue,
    currentValue,
    readOnly,
    onChange
}) => {
    return (
        <Box>
            <Editor
                className='editor'
                data-testid='editor'
                defaultValue={defaultValue}
                value={currentValue}
                readOnly={readOnly}
                readOnlyWriteCheckboxes={true}
                onChange={onChange}
            />
        </Box>
    )
}

export default MDEditor
