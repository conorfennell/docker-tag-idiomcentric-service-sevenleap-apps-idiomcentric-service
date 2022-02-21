// @ts-ignore
import Editor from 'rich-markdown-editor'
import { Box, chakra } from '@chakra-ui/react'
import './index.css'

const CEditor = chakra(Editor)

interface MDEditorProps {
    initValue: string;
    readOnly: boolean;
    onChange: (val: () => void) => void;
    onMouseUp: (text: string | null, loc: DOMRect | null) => any;
}

const MDEditor: React.FC<MDEditorProps> = ({
    initValue,
    readOnly,
    onChange,
    onMouseUp
}) => {
    return (
        <Box pos={'relative'} bg={'white'}>
            { /* @ts-ignore */ }
            <CEditor
                className='editor'
                data-testid='editor'
                handleDOMEvents={{
                    mouseup: _ => {
                        const text = window.getSelection()?.toString() || null
                        const loc = window.getSelection()?.getRangeAt(0).getBoundingClientRect() || null
                        return onMouseUp(text, loc)
                    }
                }}
                value={initValue}
                readOnly={readOnly}
                readOnlyWriteCheckboxes={true}
                onChange={onChange}
            />
        </Box>
    )
}

export default MDEditor
