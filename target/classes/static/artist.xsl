<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>
    <xsl:template match="/artist">
        <html>
            <head>
                <title>Artist Details</title>
                <style>body { font-family: Arial; } p { margin: 5px; }</style>
            </head>
            <body>
                <h1>Artist: <xsl:value-of select="fullName"/></h1>
                <p><strong>ID:</strong> <xsl:value-of select="id"/></p>
                <p><strong>Birth Year:</strong> <xsl:value-of select="birthYear"/></p>
                <h2>Pictures:</h2>
                <ul>
                    <xsl:for-each select="pictures/item">
                        <li>
                            <a href="/api/pictures/{id}"><xsl:value-of select="title"/>
                            </a> (Year: <xsl:value-of select="year"/>)</li>
                    </xsl:for-each>
                </ul>
                <nav>
                    <a href="/api/artists">All Artists</a> |
                    <a href="/api/pictures">All Pictures</a>
                </nav>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>


