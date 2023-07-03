public class IndexBlock extends Block{
    private Node nodeOfBlock;
    public IndexBlock(int blockid, String blockname) {
        super(blockid, blockname);
    }

    public void setNodeOfBlock(Node node) {
        node.setBlockid(this.getBlockid());
        this.nodeOfBlock=node;
    }
    public Node getNodeOfBlock() {
        return nodeOfBlock;
    }
}
