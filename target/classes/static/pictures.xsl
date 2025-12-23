<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>
    <xsl:template match="/ArrayList">
        <html>
            <head>
                <title>Pictures List</title>
                <style>table { border-collapse: collapse; } th, td { border: 1px solid black; padding: 8px; }</style>
            </head>
            <body>
                <h1>All Pictures</h1>
                <table>
                    <tr><th>ID</th><th>Title</th><th>Year</th><th>Artist</th></tr>
                    <xsl:for-each select="item">
                        <tr>
                            <td><xsl:value-of select="id"/></td>
                            <td><a href="/api/pictures/{id}"><xsl:value-of select="title"/></a></td>
                            <td><xsl:value-of select="year"/></td>
                            <td><a href="/api/artists/{artist/id}"><xsl:value-of select="artist/fullName"/></a></td>
                        </tr>
                    </xsl:for-each>
                </table>
                <a href="/api/artists">All Artists</a>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>


