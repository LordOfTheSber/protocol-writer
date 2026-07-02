// ExcalidrawModal намеренно НЕ реэкспортируется статически: он грузится лениво
// (см. ProtocolEditor), чтобы тяжёлый Excalidraw не попадал в основной бандл.
export { MERMAID_TEMPLATE, excalidrawBlock, appendBlock } from './model/blocks';
