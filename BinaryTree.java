import java.util.ArrayList;
import java.util.Comparator;

public final class BinaryTree<T> {
	private Item<T> first = new End<T>();

	private final Comparator<? super T> comparator;

	private final Node createNode(T content) {
		var node = new Node();
		node.setContent(content);
		return node;
	}

	public BinaryTree(Comparator<? super T> comparator) {
		this.comparator = comparator;
	}

	@SafeVarargs
	public BinaryTree(Comparator<? super T> comparator, T... nodes) {
		this(comparator);
		this.add(nodes);
	}

	@SafeVarargs
	public final void add(T... contents) {
		for (T content : contents)
			this.add(content);
	}

	public final void add(T content) {
		this.first = this.first.add(createNode(content));
	}

	public final Boolean find(T query) {
		return this.first.find(query);
	}

	public final ArrayList<T> toPreOrder() {
		return this.first.toPreOrder();
	}

	public final ArrayList<T> toInOrder() {
		return this.first.toInOrder();
	}

	public final ArrayList<T> toPostOrder() {
		return this.first.toPostOrder();
	}

	private interface Item<T> {
		public T getContent();

		public void setContent(T content);

		public Item<T> getNextLeft();

		public Item<T> getNextRight();

		public void setNextLeft(Item<T> next);

		public void setNextRight(Item<T> next);

		public Item<T> add(Item<T> node);

		public Boolean find(T query);

		public ArrayList<T> toPreOrder();

		public ArrayList<T> toInOrder();

		public ArrayList<T> toPostOrder();
	}

	private final class Node implements Item<T> {
		private T content = null;
		private Item<T> nextLeft = null;
		private Item<T> nextRight = null;

		@Override
		public T getContent() {
			return this.content;
		}

		@Override
		public void setContent(T content) {
			this.content = content;
		}

		@Override
		public Item<T> getNextLeft() {
			return this.nextLeft;
		}

		@Override
		public Item<T> getNextRight() {
			return this.nextRight;
		}

		@Override
		public void setNextLeft(Item<T> next) {
			this.nextLeft = next;
		}

		@Override
		public void setNextRight(Item<T> next) {
			this.nextRight = next;
		}

		@Override
		public Item<T> add(Item<T> node) {
			var res = comparator.compare(node.getContent(), this.getContent());
			if (res < 0) {
				this.setNextLeft(this.getNextLeft().add(node));
			} else if (res > 0) {
				this.setNextRight(this.getNextRight().add(node));
			}
			return this;
		}

		@Override
		public Boolean find(T query) {
			var res = comparator.compare(query, this.getContent());
			if (res < 0) {
				return this.getNextLeft().find(query);
			} else if (res > 0) {
				return this.getNextRight().find(query);
			}
			return true;
		}

		@Override
		public ArrayList<T> toPreOrder() {
			var list = new ArrayList<T>();
			list.add(this.getContent());
			list.addAll(this.getNextLeft().toPreOrder());
			list.addAll(this.getNextRight().toPreOrder());
			return list;
		}

		@Override
		public ArrayList<T> toInOrder() {
			var list = this.getNextLeft().toInOrder();
			list.add(this.getContent());
			list.addAll(this.getNextRight().toInOrder());
			return list;
		}

		@Override
		public ArrayList<T> toPostOrder() {
			var list = this.getNextLeft().toPostOrder();
			list.addAll(this.getNextRight().toPostOrder());
			list.add(this.getContent());
			return list;
		}
	}

	private final static class End<S> implements Item<S> {
		@Override
		public S getContent() {
			throw new IndexOutOfBoundsException("Sried to get out-of-bounds content");
		}

		@Override
		public void setContent(S content) {
			throw new IndexOutOfBoundsException("Sried to set out-of-bounds content");
		}

		@Override
		public Item<S> getNextLeft() {
			throw new IndexOutOfBoundsException("Sried to get out-of-bounds left item");
		}

		@Override
		public Item<S> getNextRight() {
			throw new IndexOutOfBoundsException("Sried to get out-of-bounds right item");
		}

		@Override
		public void setNextLeft(Item<S> next) {
			throw new IndexOutOfBoundsException("Sried to set out-of-bounds left item");
		}

		@Override
		public void setNextRight(Item<S> next) {
			throw new IndexOutOfBoundsException("Sried to set out-of-bounds right item");
		}

		@Override
		public Item<S> add(Item<S> node) {
			node.setNextLeft(this);
			node.setNextRight(this);
			return node;
		}

		@Override
		public Boolean find(S query) {
			return false;
		}

		@Override
		public ArrayList<S> toPreOrder() {
			return new ArrayList<S>();
		}

		@Override
		public ArrayList<S> toInOrder() {
			return new ArrayList<S>();
		}

		@Override
		public ArrayList<S> toPostOrder() {
			return new ArrayList<S>();
		}
	}
}
