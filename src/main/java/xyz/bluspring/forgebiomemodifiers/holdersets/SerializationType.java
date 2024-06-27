package xyz.bluspring.forgebiomemodifiers.holdersets;

/**
 * What format a holderset serializes to in json/nbt/etc
 */
public enum SerializationType
{
    /** Unhandled/unsupported holderset implementation, could serialize as potentially anything **/
    UNKNOWN,
    STRING,
    LIST,
    OBJECT;
}