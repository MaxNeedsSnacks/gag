package ky.someone.mods.gag.util;

public enum VerticalAlignment {
	TOP,
	CENTER,
	BOTTOM;

	/**
	 * Realigns the top left corner of an element according to the alignment.
	 *
	 * @param elementHeight Total height of the element
	 * @param height        Total overall height of the bounding box
	 * @return The aligned y-offset for the given element.
	 */
	public int yOffset(int elementHeight, int height) {
		return switch (this) {
			case TOP -> 0;
			case CENTER -> (height - elementHeight) / 2;
			case BOTTOM -> (height - elementHeight);
		};
	}
}
