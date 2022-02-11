import java.util.ArrayList;
import java.util.Comparator;

public class DictionaryTree<TKey, TValue> {
	private Item<TKey, TValue> first = new End<TKey, TValue>();

	private final Comparator<? super TKey> comparator;

	private final Node createNode(TKey key, TValue value) {
		var node = new Node();
		node.setKey(key);
		node.setValue(value);
		return node;
	}

	public DictionaryTree(Comparator<? super TKey> comparator) {
		this.comparator = comparator;
	}

	public final void add(TKey key, TValue value) {
		this.first = this.first.add(createNode(key, value));
	}

	public final Boolean find(TKey query) {
		return this.first.find(query);
	}

	public final TValue get(TKey key) {
		return this.first.get(key);
	}

	public final int length() {
		return this.first.length();
	}

	public final ArrayList<TKey> toPreOrder() {
		return this.first.toPreOrder();
	}

	public final ArrayList<TKey> toInOrder() {
		return this.first.toInOrder();
	}

	public final ArrayList<TKey> toPostOrder() {
		return this.first.toPostOrder();
	}

	private interface Item<TKey, TValue> {
		public TKey getKey();

		public void setKey(TKey key);

		public TValue getValue();

		public void setValue(TValue value);

		public Item<TKey, TValue> getNextLeft();

		public Item<TKey, TValue> getNextRight();

		public void setNextLeft(Item<TKey, TValue> next);

		public void setNextRight(Item<TKey, TValue> next);

		public Item<TKey, TValue> add(Item<TKey, TValue> node);

		public Boolean find(TKey query);

		public TValue get(TKey key);

		public int length();

		public ArrayList<TKey> toPreOrder();

		public ArrayList<TKey> toInOrder();

		public ArrayList<TKey> toPostOrder();
	}

	private final class Node implements Item<TKey, TValue> {
		private TKey key = null;
		private TValue value = null;
		private Item<TKey, TValue> nextLeft = null;
		private Item<TKey, TValue> nextRight = null;

		@Override
		public TKey getKey() {
			return this.key;
		}

		@Override
		public void setKey(TKey key) {
			this.key = key;
		}

		@Override
		public TValue getValue() {
			return this.value;
		}

		@Override
		public void setValue(TValue value) {
			this.value = value;
		}

		@Override
		public Item<TKey, TValue> getNextLeft() {
			return this.nextLeft;
		}

		@Override
		public Item<TKey, TValue> getNextRight() {
			return this.nextRight;
		}

		@Override
		public void setNextLeft(Item<TKey, TValue> next) {
			this.nextLeft = next;
		}

		@Override
		public void setNextRight(Item<TKey, TValue> next) {
			this.nextRight = next;
		}

		@Override
		public Item<TKey, TValue> add(Item<TKey, TValue> node) {
			var res = comparator.compare(node.getKey(), this.getKey());
			if (res < 0) {
				this.setNextLeft(this.getNextLeft().add(node));
			} else if (res > 0) {
				this.setNextRight(this.getNextRight().add(node));
			}
			return this;
		}

		@Override
		public Boolean find(TKey query) {
			var res = comparator.compare(query, this.getKey());
			if (res < 0) {
				return this.getNextLeft().find(query);
			} else if (res > 0) {
				return this.getNextRight().find(query);
			}
			return true;
		}

		@Override
		public TValue get(TKey key) {
			var res = comparator.compare(key, this.getKey());
			if (res < 0) {
				return this.getNextLeft().get(key);
			} else if (res > 0) {
				return this.getNextRight().get(key);
			}
			return this.getValue();
		}

		@Override
		public int length() {
			return this.getNextLeft().length() + this.getNextRight().length() + 1;
		}

		@Override
		public ArrayList<TKey> toPreOrder() {
			var list = new ArrayList<TKey>();
			list.add(this.getKey());
			list.addAll(this.getNextLeft().toPreOrder());
			list.addAll(this.getNextRight().toPreOrder());
			return list;
		}

		@Override
		public ArrayList<TKey> toInOrder() {
			var list = this.getNextLeft().toInOrder();
			list.add(this.getKey());
			list.addAll(this.getNextRight().toInOrder());
			return list;
		}

		@Override
		public ArrayList<TKey> toPostOrder() {
			var list = this.getNextLeft().toPostOrder();
			list.addAll(this.getNextRight().toPostOrder());
			list.add(this.getKey());
			return list;
		}
	}

	private final static class End<SKey, SValue> implements Item<SKey, SValue> {
		@Override
		public SKey getKey() {
			throw new IndexOutOfBoundsException("Tried to get out-of-bounds key");
		}

		@Override
		public void setKey(SKey key) {
			throw new IndexOutOfBoundsException("Tried to set out-of-bounds key");
		}

		@Override
		public SValue getValue() {
			throw new IndexOutOfBoundsException("Tried to get out-of-bounds value");
		}

		@Override
		public void setValue(SValue value) {
			throw new IndexOutOfBoundsException("Tried to set out-of-bounds value");
		}

		@Override
		public Item<SKey, SValue> getNextLeft() {
			throw new IndexOutOfBoundsException("Tried to get out-of-bounds left item");
		}

		@Override
		public Item<SKey, SValue> getNextRight() {
			throw new IndexOutOfBoundsException("Tried to get out-of-bounds right item");
		}

		@Override
		public void setNextLeft(Item<SKey, SValue> next) {
			throw new IndexOutOfBoundsException("Tried to set out-of-bounds left item");
		}

		@Override
		public void setNextRight(Item<SKey, SValue> next) {
			throw new IndexOutOfBoundsException("Tried to set out-of-bounds right item");
		}

		@Override
		public Item<SKey, SValue> add(Item<SKey, SValue> node) {
			node.setNextLeft(this);
			node.setNextRight(this);
			return node;
		}

		@Override
		public Boolean find(SKey query) {
			return false;
		}

		@Override
		public SValue get(SKey key) {
			return null;
		}

		@Override
		public int length() {
			return 0;
		}

		@Override
		public ArrayList<SKey> toPreOrder() {
			return new ArrayList<SKey>();
		}

		@Override
		public ArrayList<SKey> toInOrder() {
			return new ArrayList<SKey>();
		}

		@Override
		public ArrayList<SKey> toPostOrder() {
			return new ArrayList<SKey>();
		}
	}
}
