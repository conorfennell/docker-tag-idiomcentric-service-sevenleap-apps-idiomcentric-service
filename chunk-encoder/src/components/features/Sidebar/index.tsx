import { Box, Flex, Text, CloseButton, useColorModeValue } from '@chakra-ui/react'

interface SidebarProps {
    onClose: () => void;
    display?: any;
}

const Sidebar: React.FC<SidebarProps> = ({ children, onClose, ...rest }) => {
    return (
        <Box
            bg={useColorModeValue('white', 'gray.900')}
            borderRight="1px"
            borderRightColor={useColorModeValue('gray.200', 'gray.700')}
            w={{ base: 'full', md: 80 }}
            pos="fixed"
            h="full"
            {...rest}
        >
            <Flex h="20" alignItems="center" mx="8" justifyContent="space-between">
                <Text fontSize="2xl" fontFamily="monospace" fontWeight="bold">
                    Chunks
                </Text>
                <CloseButton display={{ base: 'flex', md: 'none' }} onClick={onClose} />
            </Flex>
            {children}
        </Box>
    )
}

export default Sidebar
