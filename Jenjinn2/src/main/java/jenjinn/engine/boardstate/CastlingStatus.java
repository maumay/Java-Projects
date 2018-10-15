/**
 *
 */
package jenjinn.engine.boardstate;

import static jflow.utilities.CollectionUtil.string;

import java.util.EnumSet;
import java.util.Set;

import jenjinn.engine.base.CastleZone;
import jenjinn.engine.base.Side;

/**
 * @author ThomasB
 */
public final class CastlingStatus
{
	private final Set<CastleZone> castlingRights;
	private CastleZone whiteCastlingStatus, blackCastlingStatus;

	public CastlingStatus(Set<CastleZone> castlingRights, CastleZone whiteCastlingStatus, CastleZone blackCastlingStatus)
	{
		this.castlingRights = castlingRights;
		this.whiteCastlingStatus = whiteCastlingStatus;
		this.blackCastlingStatus = blackCastlingStatus;
	}

	public Set<CastleZone> getCastlingRights()
	{
		return castlingRights;
	}

	public CastleZone getWhiteCastlingStatus()
	{
		return whiteCastlingStatus;
	}

	public CastleZone getBlackCastlingStatus()
	{
		return blackCastlingStatus;
	}

	public CastleZone getStatusFor(Side side)
	{
		return side.isWhite()? whiteCastlingStatus : blackCastlingStatus;
	}

	public void setCastlingStatus(CastleZone newStatus)
	{
		if (newStatus.isWhiteZone()) {
			assert whiteCastlingStatus == null;
			whiteCastlingStatus = newStatus;
		}
		else {
			assert blackCastlingStatus == null;
			blackCastlingStatus = newStatus;
		}
	}

	public void removeCastlingStatus(CastleZone toRemove)
	{
		if (toRemove.isWhiteZone()) {
			assert toRemove == whiteCastlingStatus;
			whiteCastlingStatus = null;
		}
		else {
			assert toRemove == blackCastlingStatus;
			blackCastlingStatus = null;
		}
	}

	@Override
	public String toString()
	{
		return new StringBuilder("CastlingStatus[Castling rights: ")
				.append(string(castlingRights))
				.append(", White status: ")
				.append(string(whiteCastlingStatus))
				.append(", Black status: ")
				.append(string(blackCastlingStatus))
				.append("]")
				.toString();
	}

	public CastlingStatus copy()
	{
		return new CastlingStatus(EnumSet.copyOf(castlingRights), whiteCastlingStatus, blackCastlingStatus);
	}

	/*
	 * Generated by Eclipse.
	 */
	@Override
	public int hashCode()
	{
		int prime = 31;
		int result = 1;
		result = prime * result + ((blackCastlingStatus == null) ? 0 : blackCastlingStatus.hashCode());
		result = prime * result + ((castlingRights == null) ? 0 : castlingRights.hashCode());
		result = prime * result + ((whiteCastlingStatus == null) ? 0 : whiteCastlingStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CastlingStatus other = (CastlingStatus) obj;
		if (blackCastlingStatus != other.blackCastlingStatus)
			return false;
		if (castlingRights == null) {
			if (other.castlingRights != null)
				return false;
		} else if (!castlingRights.equals(other.castlingRights))
			return false;
		if (whiteCastlingStatus != other.whiteCastlingStatus)
			return false;
		return true;
	}
}
