<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>
    <xsl:template match="/picture">
        <html>
            <head>
                <title>Picture Details</title>
                <style>body { font-family: Arial; } p { margin: 5px; }</style>
            </head>
            <body>
                <h1>Picture: <xsl:value-of select="title"/></h1>
                <p><strong>ID:</strong> <xsl:value-of select="id"/></p>
                <p><strong>Year:</strong> <xsl:value-of select="year"/></p>
                <p>
                    <strong>Artist:</strong>
                    <a href="/api/artists/{artist/id}">
                        <xsl:value-of select="artist/fullName"/>
                    </a> (Birth Year: <xsl:value-of select="artist/birthYear"/>)
                </p>
                <nav>
                    <a href="/api/pictures">All Pictures</a> |
                    <a href="/api/artists">All Artists</a>
                </nav>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>


